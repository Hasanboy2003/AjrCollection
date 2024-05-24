package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountCreateDto;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountUpdateDto;
import com.yurakamri.ajrcollection.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    /**
     * GET DISCOUNTS
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getDiscounts()
           {
        return discountService.getDiscounts();
    }

    /**
     * GET SINGLE DISCOUNT
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDiscount(@PathVariable Long id) {
        return discountService.getDiscount(id);
    }

    /**
     * CREATE DISCOUNT WITH FEW PRODUCTS OR ITSELF
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createDiscount(@Valid @RequestBody DiscountCreateDto dto) {
        return discountService.createDiscount(dto);
    }

    /**
     * UPDATE SPECIFIC DISCOUNT
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDiscount(@Valid @RequestBody DiscountUpdateDto src, @PathVariable Long id,@RequestParam UUID productId) {
        return discountService.editDiscount(id, src,productId);
    }

    /**
     * DELETE SPECIFIC DISCOUNT
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDiscount(@PathVariable Long id,@RequestParam UUID productId) {
        return discountService.delete(id,productId);
    }

    /**
     * PUT SPECIFIC PRODUCT ON DISCOUNT
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/{discountId}/put/{productId}")
    public ResponseEntity<ApiResponse> putOnDiscount(@PathVariable Long discountId, @PathVariable UUID productId) {
        return discountService.putOnDiscount(discountId, productId);
    }

    /**
     * DELETES DISCOUNT FROM SPECIFIC PRODUCT
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/take/{productId}")
    public ResponseEntity<ApiResponse> takeFromDiscount(@PathVariable UUID productId) {
        return discountService.takeFromDiscount(productId);
    }

}
