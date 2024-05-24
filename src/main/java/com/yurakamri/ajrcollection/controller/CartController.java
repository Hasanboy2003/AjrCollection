package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CartDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(BaseUrl.BASE_URL + "/cart")
public class CartController {

    final CartService cartService;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("/mobile")
    public HttpEntity<?> create(@Valid @RequestBody CartDto cartDto, @CurrentUser User user) {
        ApiResponse response = cartService.create(cartDto, user);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }



    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PutMapping("/mobile")
    public HttpEntity<?> update(@Valid @RequestBody CartDto cartDto, @CurrentUser User user) {
        ApiResponse response = cartService.update(cartDto, user);
        return ResponseEntity.status(response.isSuccess() ? 202 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/mobile/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse response = cartService.getById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/mobile")
    public HttpEntity<?> getUserCards(@CurrentUser User user) {
        ApiResponse response = cartService.getByUserId(user.getId());
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping("/mobile/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse response = cartService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping("/mobile")
    public HttpEntity<?> deleteUserCarts(@CurrentUser User user) {
        ApiResponse response = cartService.deleteUserCarts(user);
        return ResponseEntity.status(response.isSuccess() ? 202 : 409).body(response);
    }

    @PatchMapping
    public HttpEntity<?> patch(@RequestParam int quantity, @RequestParam UUID cartId, @CurrentUser User user) {
        ApiResponse response = cartService.patch(quantity, cartId, user);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }
}
