package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.product.ProductDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.ProductService;
import com.yurakamri.ajrcollection.utills.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(BaseUrl.BASE_URL + "/product")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllProductForAdmin(@RequestParam boolean all) {
        ApiResponse response = productService.getAllProductByAdmin(all);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdProduct(@PathVariable UUID id) {
        ApiResponse response = productService.getByIdProduct(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public HttpEntity<?> addProduct(@Valid @RequestBody ProductDto productDto) {
        ApiResponse apiResponse = productService.addProduct(productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        ApiResponse apiResponse = productService.editProduct(id, productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.deleteProduct(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/editprice/{id}")
    public HttpEntity<?> editPrice(@PathVariable UUID id, @RequestParam Double newPrice) {
        ApiResponse apiResponse = productService.outcomePriceEdit(id, BigDecimal.valueOf(newPrice));
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/enabledordisabled/{id}")
    public HttpEntity<?> enabledOrDisabled(@PathVariable UUID id, @RequestParam boolean active) {
        ApiResponse apiResponse = productService.enabledOrDisabled(id, active);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/productcolors/{id}")
    public HttpEntity<?> getColorByProduct(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.getProductColors(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/mobile")
    public HttpEntity<?> getAllProductForUser(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                              @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                              @CurrentUser User user) {
        ApiResponse apiResponse = productService.getAllProduct(user, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/mobile/{id}")
    public HttpEntity<?> getByIdProductForUser(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = productService.getByIdProductForUser(user, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/mobile/getAllProductByBrandOrCategory")
    public HttpEntity<?> getAllProductByBrandOrCategory(
            @CurrentUser User user, @RequestParam(defaultValue = "") UUID categoryId,
            @RequestParam(defaultValue = "") Long brandId,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        ApiResponse apiResponse = productService.getAllByCategoryIdOrBrandId(categoryId, brandId, user, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(BaseUrl.M + "/search/history")
    public ResponseEntity<ApiResponse> searchHistory(@CurrentUser User user) {
        return productService.getSearchHistory(user);
    }

    @GetMapping(BaseUrl.M + "/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(name = "searchText") @NotBlank(message = "search text must not be blank") String searchText,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @AuthenticationPrincipal UserDetails user
    ) throws PageSizeException {
        return productService.search(searchText, page, size, user);
    }

}
