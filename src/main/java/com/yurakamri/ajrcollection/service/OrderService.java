package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.*;
import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CartDto;
import com.yurakamri.ajrcollection.payload.OrderDto;
import com.yurakamri.ajrcollection.payload.UserOrderDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceOrderDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.OrderProjection;
import com.yurakamri.ajrcollection.payload.userForAdmin.UserDTOForAdmin;
import com.yurakamri.ajrcollection.repository.*;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.yurakamri.ajrcollection.utills.AppConstants.BASE_INCOME_CODE_PREFIX;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderService {
    final CartRepo cartRepo;
    final OrderRepo orderRepo;
    final OrderStatusRepo orderStatusRepo;
    final UserRepo userRepo;
    final ProductRepository productRepository;
    final ProductColorRepository productColorRepository;
    final SizeRepository sizeRepository;
    final ColorRepo colorRepo;


    public ApiResponse create(OrderDto dto, User user) {
        List<CartItem> cartItems;
//        if (dto.getCharityCart() && dto.getMyCart())
//            cartItems = cartRepo.getAllByUserIdAndOrderIsNull(user.getId());
//        else if (dto.getMyCart())
        //charity stopped temporarily
        // only user cart
            cartItems = cartRepo.findByUserIdAndOrderIsNullAndForCharityIsFalse(user.getId());
//        else
//            cartItems = cartRepo.findByUserIdAndOrderIsNullAndForCharityIsTrue(user.getId());
        if (cartItems.isEmpty())
            return new ApiResponse(false, "YOU DON'T HAVE PRODUCTS IN CART");

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProductColor().getProduct();
            Color color = cartItem.getProductColor().getColor();
            Size size = cartItem.getSize();
            Integer quantity = cartRepo.findQuantityByProductIdAndColorIdAndSizeId(product.getId(), color.getId(), size.getId());
            if (quantity <= 0) {
                return new ApiResponse(false, "There is not " + product.getName() + " with these " + size.getName() + " size in shop", color.getCode());
            } else if (quantity < cartItem.getQuantity())
                return new ApiResponse(false, "Quantity of " + product.getName() + " with these " + size.getName() + " size and color is not enough for your order", color.getCode());
        }

        Order order = new Order(orderStatusRepo.getByName(OrderStatusEnum.NEW),
                user,
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getShippingAmount(),
                generateOrderCode(),
                dto.getAddress(), dto.getPhoneNumber()
        );

        Order savedOrder = orderRepo.save(order);

        for (CartItem cartItem : cartItems) {
            cartItem.setOrder(savedOrder);
            Product product = cartItem.getProductColor().getProduct();
            Discount discount = product.getDiscount();
            boolean valid = discount != null && DAYS.between(LocalDate.now(),discount.getEndDate()) >= 0;
            double value = product.getOutcomePrice().doubleValue();
            cartItem.setFixedPrice(valid ? new BigDecimal(value - (value * discount.getPercent().doubleValue() / 100)) : product.getOutcomePrice());
            cartItem.setDiscount(valid ? discount : null);
        }
        cartRepo.saveAll(cartItems);
        return new ApiResponse(true, "ORDER IS CREATED");
    }

    
    public ApiResponse getAll(int page, int size) {
        try {
            CommandUtils.validatePageAndSize(page, size);
        } catch (PageSizeException e) {
            return new ApiResponse(false, e.getMessage());
        }
        if (page > 0)
            page--;
        page = size * page;
        List<InterfaceOrderDto> orders = orderRepo.getOrders(size, page);

//        List<InterfaceOrderDto> orders = orderRepo.findOrders();
        return new ApiResponse(true, "ALL ORDERS", orders);
    }

    public ApiResponse getById(UUID id) {
        if (!orderRepo.existsById(id))
            return new ApiResponse(false, "ORDER DOES NOT EXIST");
        return new ApiResponse(true, "ORDER BY ID", generateOrderDto(orderRepo.getById(id)));
    }

    OrderDto generateOrderDto(Order order) {
        return new OrderDto(order.getId(),
                order.getOrderStatus().getName().toString(),
                order.getLongitude(),
                order.getLatitude(),
                order.getDriver().getId(),
                order.getShippingAmount(),
                order.getCode(), order.getAddress(),order.isCash(),
                order.getPhoneNumber());
    }

    public ApiResponse getUserOrders(User user) {
        if (!userRepo.existsById(user.getId()))
            return new ApiResponse(false, "USER DOES NOT EXIST");
        List<InterfaceOrderDto> orderDtoList = orderRepo.findByUserId(user.getId());
        return new ApiResponse(true, "USER ORDERS", orderDtoList);
    }

    public ApiResponse createOrder(List<CartDto> cartDtoList, User user) {

        for (CartDto cartDto : cartDtoList) {
            Integer quantity = cartRepo.findQuantityByProductIdAndColorIdAndSizeId(cartDto.getProductId(), cartDto.getColorId(), cartDto.getSizeId());
            if (quantity <= 0) {
                return new ApiResponse(false, "There is not "
                        + productRepository.getById(cartDto.getProductId()).getName()
                        + " with these "
                        + sizeRepository.getById(cartDto.getSizeId()).getName()
                        + " size in shop", colorRepo.getById(cartDto.getColorId()).getCode());
            } else if (quantity < cartDto.getQuantity())
                return new ApiResponse(false, "Quantity of "
                        + productRepository.getById(cartDto.getProductId()).getName()
                        + " with these "
                        + sizeRepository.getById(cartDto.getSizeId()).getName()
                        + " size and color is not enough for your order", colorRepo.getById(cartDto.getColorId()).getCode());
        }

        Order order = new Order(orderStatusRepo.getByName(OrderStatusEnum.SOLD), user, generateOrderCode(), user.getPhoneNumber());

        orderRepo.saveAndFlush(order);
        List<CartItem> cartItemList = new ArrayList<>();
        for (CartDto cartDto : cartDtoList) {
            ProductColor productColor = productColorRepository.getById(cartDto.getId());
            Size size = sizeRepository.getById(cartDto.getSizeId());
            Discount discount = productColor.getProduct().getDiscount();
            boolean valid = discount != null && DAYS.between(LocalDate.now(), discount.getEndDate()) >= 0;
            double value = cartDto.getFixedPrice().doubleValue();
            cartItemList.add(new CartItem(user,
                    productColor,
                    size, cartDto.getQuantity(),
                    valid ? discount : null,
                    valid ? new BigDecimal(value - (value * discount.getPercent().doubleValue() / 100)) : cartDto.getFixedPrice(),
                    order,
                    cartDto.isForCharity()));
        }
        cartRepo.saveAll(cartItemList);
        return new ApiResponse(true,"ORDERS ARE SAVED!");
    }

    private String generateOrderCode() {
        LocalDate currentDate = LocalDate.now();
        int nextIncomeCodeForDate = orderRepo.findLastOrderCodeForDate(currentDate) + 1;
        return BASE_INCOME_CODE_PREFIX +
                currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "0".repeat(6 - String.valueOf(nextIncomeCodeForDate).length()) +
                nextIncomeCodeForDate;
    }

    public ApiResponse getUserOrdersForAdmin(UUID userId) {
        if (!userRepo.existsById(userId))
            return new ApiResponse(false, "USER DOES NOT EXIST");
        User user = userRepo.getById(userId);
        List<OrderProjection> orderDtoList = orderRepo.getByUserId(userId);
        UserDTOForAdmin userDto = new UserDTOForAdmin(userId, user.getPhoneNumber(), user.getFirstName(), user.getLastName(), user.isEnabled());
        return new ApiResponse(true, "ALL USER ORDERS",new UserOrderDto(userDto,orderDtoList));
    }

    public ApiResponse setProvider(List<UUID> orderIdList, UUID providerId) {
        Optional<User> optionalDriver = userRepo.findByIdAndRoleRoleName(providerId, RoleEnum.ROLE_DELIVERER);
        if (optionalDriver.isEmpty())
            return new ApiResponse(false, "DRIVER NOT FOUND");
        User driver = optionalDriver.get();
        List<Order> orderList = orderRepo.findAllById(orderIdList);
        boolean allMatch = orderList.stream().allMatch(order -> orderRepo.existsById(order.getId()) &&
                order.getOrderStatus().getName().toString().equals(OrderStatusEnum.NEW.toString()));

        if (!allMatch)
            return new ApiResponse(false, "one of new orders is not available");
        orderList.forEach(order -> order.setDriver(driver));
        orderList.forEach(order -> order.setOrderStatus(orderStatusRepo.getByName(OrderStatusEnum.IN_PROGRESS)));
        orderRepo.saveAll(orderList);
        return new ApiResponse(true, "Provider is attached to orders");
    }

    public ApiResponse reject(UUID orderId, User user) {
        if (!orderRepo.existsById(orderId))
            return new ApiResponse(false, "ORDER DOES NOT EXIST");
        Order order = orderRepo.getById(orderId);
        if (!order.getOrderStatus().getName().name().equals("NEW"))
            return new ApiResponse(false, "YOU CAN REJECT ONLY NEW ORDER");
        if (!orderRepo.existsByUserId(user.getId()))
            return new ApiResponse(false, "THIS ORDER DOES NOT BELONG TO YOU");
        order.setOrderStatus(orderStatusRepo.getByName(OrderStatusEnum.REJECTED));
        orderRepo.save(order);
        return new ApiResponse(true, "ORDER IS REJECTED");
    }

}
