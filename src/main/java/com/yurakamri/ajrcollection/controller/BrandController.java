package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.Brand;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.BrandDto;
import com.yurakamri.ajrcollection.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;


    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAllBrand(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        ApiResponse response = brandService.getAllBrand(page, size);

        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public HttpEntity<?> getByIdBrand(@PathVariable Long id) {
        ApiResponse apiResponse = brandService.getByIdBrand(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> addBrand(@Valid @RequestBody BrandDto brandDto) {
        ApiResponse apiResponse = brandService.addBrand(brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/web/")
    public HttpEntity<?> editBrand(@RequestBody BrandDto brandDto) {
        ApiResponse apiResponse = brandService.editBrand(brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> deleteBrand(@PathVariable Long id) {
        ApiResponse apiResponse = brandService.deleteBrand(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web/enabledordisabled/{id}")
    public HttpEntity<?> enabledOrDisabled(@PathVariable Long id, @RequestParam boolean isActive) {
        ApiResponse apiResponse = brandService.enabledOrDisabled(id, isActive);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
