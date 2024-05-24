package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Order;
import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.DeliverDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceDriverOrderDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceOrderDto;
import com.yurakamri.ajrcollection.repository.OrderRepo;
import com.yurakamri.ajrcollection.repository.OrderStatusRepo;
import com.yurakamri.ajrcollection.repository.RoleRepo;
import com.yurakamri.ajrcollection.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final OrderRepo orderRepo;
    private final OrderStatusRepo orderStatusRepo;


    public ApiResponse addDeliverer(DeliverDto dto) {
        boolean existsByPhoneNumber = userRepo.existsByPhoneNumber(dto.getPhoneNumber());
        if (existsByPhoneNumber) {
            return new ApiResponse(false, "Siz kiritgan telifon raqamli foydalanuvchi mavjud");
        }
        User user = new User(dto.getPhoneNumber(), dto.getFirstName(), dto.getLastName(), null, roleRepo.findByRoleName(RoleEnum.ROLE_DELIVERER).get());
        userRepo.save(user);
        return new ApiResponse(true, "Haydovchi qo`shildi");
    }

    public ApiResponse deleteDeliverer(UUID id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            if (userOptional.get().getRole().getRoleName() == RoleEnum.ROLE_DELIVERER) {
                try {
                    userRepo.deleteById(id);
                    return new ApiResponse(true, "Haydovchi o`chirildi");
                } catch (Exception e) {
                    String phoneNumber = userOptional.get().getPhoneNumber();
                    userOptional.get().setPhoneNumber(phoneNumber + UUID.randomUUID());
                    userOptional.get().setEnabled(false);
                    userRepo.save(userOptional.get());
                    return new ApiResponse(true, "Havdovchi faolligi o`chirildi ");
                }
            }
        }
        return new ApiResponse(false, "Siz so`ragan foydalanuvchi mavjud emas");

    }

    public ApiResponse enableOrDisabled(UUID id, boolean active) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(active);
            userRepo.save(user);
            return new ApiResponse(true, "Havdovchi " + (active ? "aktivlashtirildi" : "bloklandi") + ".");
        }
        return new ApiResponse(false, "Siz so`ragan Haydovchi mavjud emas");
    }

    public ApiResponse getAllDeliverer() {
        List<User> delivers = userRepo.findAllByRoleRoleName(RoleEnum.ROLE_DELIVERER);
        List<DeliverDto> collect = delivers.stream().map(this::generateDeliverDto).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return new ApiResponse(true, "ALL DELIVERERS", collect);
        }
        return new ApiResponse(true, "DELIVERS DOES NOT EXIST");
    }

    public DeliverDto generateDeliverDto(User user) {
        return new DeliverDto(user.getId(), user.getPhoneNumber(), user.getFirstName(), user.getLastName(), user.getAttachment() != null ? user.getAttachment().getId() : null, user.getCreatedAt(), user.isEnabled());
    }

    public ApiResponse getById(UUID id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getRole().getRoleName() == RoleEnum.ROLE_DELIVERER) {
                DeliverDto deliverDto = generateDeliverDto(optionalUser.get());
                return new ApiResponse(true, "DELIVER BY ID", deliverDto);
            }
        }
        return new ApiResponse(false, "DELIVER NOT FOUND");
    }

    public ApiResponse getDriverOrders(User user) {
        List<InterfaceOrderDto> orderList = orderRepo.findByDriverId(user.getId());
        if (orderList.isEmpty())
            return new ApiResponse(false, "Driver orders do not exist!");
        return new ApiResponse(true, "DRIVER ORDERS", orderList);
    }

    public ApiResponse findDriverOrders(User user) {
        List<InterfaceDriverOrderDto> orderList = orderRepo.getByDriverId(user.getId());
        if (orderList.isEmpty())
            return new ApiResponse(false, "Driver orders do not exist!");
        return new ApiResponse(true, "DRIVER ORDERS", orderList);
    }
    public ApiResponse accept(UUID orderId) {
        if (!orderRepo.existsById(orderId))
            return new ApiResponse(false, "ORDER DOES NOT EXIST");
        Order order = orderRepo.getById(orderId);
        if (!order.getOrderStatus().getName().name().equals("IN_PROGRESS"))
            return new ApiResponse(false, "YOU CAN ACCEPT ONLY IN_PROGRESS ORDER");
        order.setOrderStatus(orderStatusRepo.getByName(OrderStatusEnum.DELIVERED));
        orderRepo.save(order);
        return new ApiResponse(true, "ORDER IS DELIVERED");
    }

    public ApiResponse rejectByDriver(UUID orderId, User user) {
        if (!orderRepo.existsById(orderId))
            return new ApiResponse(false, "ORDER DOES NOT EXIST");
        Order order = orderRepo.getById(orderId);
        if (!order.getOrderStatus().getName().name().equals("IN_PROGRESS"))
            return new ApiResponse(false, "YOU CAN REJECT ONLY IN_PROGRESS ORDER");
        if (!orderRepo.existsByDriverId(user.getId()))
            return new ApiResponse(false, "THIS ORDER DOES NOT BELONG TO YOU");
        order.setOrderStatus(orderStatusRepo.getByName(OrderStatusEnum.REJECTED));
        orderRepo.save(order);
        return new ApiResponse(true, "ORDER IS REJECTED");
    }
}
