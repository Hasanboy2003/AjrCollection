package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.Category;
import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.entity.Size;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CategoryDto;
import com.yurakamri.ajrcollection.payload.product.ProductDto;
import com.yurakamri.ajrcollection.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BaseUrl.BASE_URL+"/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAllCategory(){
        ApiResponse response = categoryService.getAllCategory();
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public ResponseEntity<?> getByIdCategory(@PathVariable UUID id){
        ApiResponse response = categoryService.getByIdCategory(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> addCategory(@Valid @RequestBody CategoryDto categoryDto){
        ApiResponse apiResponse=categoryService.addCategory(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/web/{id}")
    public HttpEntity<?> editCategory(@PathVariable UUID id, @Valid @RequestBody CategoryDto categoryDto){
        ApiResponse apiResponse=categoryService.editCategory(id, categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable UUID id){
        ApiResponse apiResponse=categoryService.deleteCategory(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web/enabledordisabled/{id}")
    public HttpEntity<?> enabledOrDisabled(@PathVariable UUID id, boolean active){
        ApiResponse apiResponse=categoryService.enabledOrDisabled(id, active);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    @GetMapping("/mobile")
    public HttpEntity<?> getAllCategoryForUser(){
        ApiResponse apiResponse=categoryService.getAllCategoryForUser();
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    @GetMapping
    public HttpEntity<?> getAllCategoryByBrandId (@RequestParam Long brandId){
        ApiResponse apiResponse=categoryService.getAllCategoryByBrandId(brandId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }






}
