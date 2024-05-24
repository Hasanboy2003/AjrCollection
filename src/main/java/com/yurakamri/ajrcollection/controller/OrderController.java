package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CartDto;
import com.yurakamri.ajrcollection.payload.OrderDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(BaseUrl.BASE_URL+"/order")
public class OrderController {

    final OrderService orderService;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("/mobile")
    public HttpEntity<?> create(@RequestBody OrderDto dto, @CurrentUser User user){
        ApiResponse response = orderService.create(dto,user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }


    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/mobile")
    public HttpEntity<?> getUserOrders(@CurrentUser User user){
        ApiResponse response = orderService.getUserOrders(user);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> createOrder(@RequestBody List<CartDto> cartDtoList, @CurrentUser User user){
        ApiResponse response = orderService.createOrder(cartDtoList,user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam (defaultValue = "10")int size){
        ApiResponse response = orderService.getAll(page,size);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id){
        ApiResponse response = orderService.getById(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/userOrders")
    public HttpEntity<?> getUserOrdersForAdmin(@RequestParam UUID userId){
        ApiResponse response = orderService.getUserOrdersForAdmin(userId);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web/{providerId}")
    HttpEntity<?> setProviderToOrder(@PathVariable UUID providerId,@RequestParam List<UUID> orderIdList){
        ApiResponse response = orderService.setProvider(orderIdList, providerId);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PatchMapping("/mobile/reject/user/{orderId}")
    public HttpEntity<?> rejectOrder(@PathVariable UUID orderId,@CurrentUser User user){
        ApiResponse response = orderService.reject(orderId,user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

}
