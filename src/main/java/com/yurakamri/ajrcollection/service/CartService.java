package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.*;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CartDto;
import com.yurakamri.ajrcollection.payload.product.ColorAttachmentDto;
import com.yurakamri.ajrcollection.payload.userForAdmin.OrderDetailDto;
import com.yurakamri.ajrcollection.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartService {
    final CartRepo cartRepo;
    final UserRepo userRepo;
    final SizeRepository sizeRepository;
    final ProductColorRepository productColorRepository;
    final ProductAttachmentRepository productAttachmentRepository;

    public ApiResponse create(CartDto dto, User user) {
        Optional<Size> optionalSize = sizeRepository.findById(dto.getSizeId());
        Optional<ProductColor> optionalProductColor = productColorRepository.findByColorIdAndProductId(dto.getColorId(), dto.getProductId());
        ApiResponse validate = validate(optionalSize, optionalProductColor);
        if (!validate.isSuccess())
            return validate;
        Size size = optionalSize.get();
        ProductColor productColor = optionalProductColor.get();
        CartItem cartItem;
        Optional<CartItem> optionalCartItem = cartRepo.findByUserIdAndSizeIdAndProductColorIdAndOrderIsNullAndForCharity(user.getId(), size.getId(), productColor.getId(), dto.isForCharity());
        if (optionalCartItem.isPresent()) {
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
        } else
            cartItem = new CartItem(user, productColor, size, dto.getQuantity(), dto.isForCharity());

        cartRepo.save(cartItem);
        return new ApiResponse(true, "PRODUCT IS ADDED TO USER CART");
    }

    public ApiResponse update(CartDto dto, User user) {
        if (!cartRepo.existsById(dto.getId()))
            return new ApiResponse(false, "USER CART DOES NOT EXIST");

        Optional<Size> optionalSize = sizeRepository.findById(dto.getSizeId());
        Optional<ProductColor> optionalProductColor = productColorRepository.findById(dto.getProductId());

        ApiResponse validate = validate(optionalSize, optionalProductColor);
        if (!validate.isSuccess())
            return validate;
        CartItem cartItem = cartRepo.getById(dto.getId());
        Size size = optionalSize.get();
        ProductColor productColor = optionalProductColor.get();
        cartItem.setFixedPrice(dto.getFixedPrice());
        cartItem.setForCharity(dto.isForCharity());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setProductColor(productColor);
        cartItem.setSize(size);
        cartItem.setUser(user);
        cartRepo.save(cartItem);
        return new ApiResponse(true, "USER CART IS UPDATED");
    }

    public ApiResponse getById(UUID id) {
        if (!cartRepo.existsById(id))
            return new ApiResponse(false, "CART DOES NOT EXIST");
        return new ApiResponse(true, "CART BY ID", generateOrderDetailDto(cartRepo.getById(id)));
    }

    OrderDetailDto generateOrderDetailDto(CartItem item) {
        Size size = item.getSize();
        ProductColor productColor = item.getProductColor();
        Product product = productColor.getProduct();
        Discount discount = product.getDiscount();
        boolean valid = discount != null && DAYS.between(LocalDate.now(), discount.getEndDate()) >= 0;
        double value = product.getOutcomePrice().doubleValue();
        List<UUID> attachmentIdList = new LinkedList<>();
        List<ProductAttachment> productAttachments = productAttachmentRepository.findAllByProductColorId(productColor.getId());
        productAttachments.forEach(productAttachment -> attachmentIdList.add(productAttachment.getAttachment().getId()));
        ColorAttachmentDto colorAttachmentDto = new ColorAttachmentDto(productColor.getColor().getId(), productColor.getColor().getCode(), new HashSet<>(attachmentIdList));
        return new OrderDetailDto(item.getId(), product.getId(),
                product.getName(), product.getDescription(),
                valid ? new BigDecimal(value - (value * discount.getPercent().doubleValue() / 100)) : product.getOutcomePrice(),
                size.getName(), product.getBrand().getName(),
                item.getQuantity(), product.getOutcomePrice(), item.isForCharity(),
                Collections.singleton(colorAttachmentDto));
    }

    public ApiResponse getByUserId(UUID userId) {
        if (!userRepo.existsById(userId))
            return new ApiResponse(false, "USER DOES NOT EXIST");
        List<OrderDetailDto> orderDetailDtos = cartRepo.getAllByUserIdAndOrderIsNull(userId).stream().map(this::generateOrderDetailDto).collect(Collectors.toList());
        if (orderDetailDtos.isEmpty())
            return new ApiResponse(false, "PRODUCT DOES NOT EXIST IN YOUR CART");
        return new ApiResponse(true, "USER CART ITEMS", orderDetailDtos);
    }

    @Transactional
    public ApiResponse delete(UUID id) {
        if (!cartRepo.existsById(id))
            return new ApiResponse(false, "CART ID DOES NOT EXIST");
        CartItem cartItem = cartRepo.getById(id);
        if (cartItem.getOrder() != null)
            return new ApiResponse(false, "ALREADY ORDERED");
        cartRepo.deleteById(id);
        return new ApiResponse(true, "CART IS DELETED");
    }

    @Transactional
    public ApiResponse deleteUserCarts(User user) {
        if (!userRepo.existsById(user.getId()))
            return new ApiResponse(false, "USER DOES NOT EXIST!");
        cartRepo.deleteAllByUserIdAndOrderIsNull(user.getId());
        return new ApiResponse(true, "USER CARTS ARE DELETED");
    }

    private ApiResponse validate(Optional<Size> optionalSize, Optional<ProductColor> optionalProductColor) {
        if (optionalSize.isEmpty())
            return new ApiResponse(false, "SIZE DOES NOT EXIST");
        if (optionalProductColor.isEmpty())
            return new ApiResponse(false, "PRODUCT COLOR DOES NOT EXIST");
        return new ApiResponse(true, "");
    }

    public ApiResponse patch(int quantity, UUID cartId, User user) {
        if (!cartRepo.existsById(cartId))
            return new ApiResponse(false, "CART ID DOES NOT EXIST");
        Optional<CartItem> optionalCartItem = cartRepo.findByIdAndUserId(cartId, user.getId());
        if (optionalCartItem.isEmpty())
            return new ApiResponse(false, "CART DOES NOT BELONG TO THIS USER");
        CartItem cartItem = optionalCartItem.get();
        cartItem.setQuantity(quantity);
        cartRepo.save(cartItem);
        return new ApiResponse(true, "SUCCESSFULLY CHANGED");
    }


}
