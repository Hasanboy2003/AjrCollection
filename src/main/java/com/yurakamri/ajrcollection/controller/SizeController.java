package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.Size;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.SizeDto;
import com.yurakamri.ajrcollection.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/size")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAllSize(){
        ApiResponse response = sizeService.getAllSize();
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public ResponseEntity<?> getByIdSize(@PathVariable Long id){
        ApiResponse response = sizeService.getByIdSize(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> addSize(@Valid @RequestBody SizeDto sizeDto){
        ApiResponse apiResponse=sizeService.addSize(sizeDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/web/{id}")
    public HttpEntity<?> editSize(@PathVariable Long id, @RequestBody SizeDto sizeDto){
        ApiResponse apiResponse=sizeService.editSize(id, sizeDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> deleteSize(@PathVariable Long id){
        ApiResponse apiResponse=sizeService.deleteSize(id);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web")
    public HttpEntity<?> patch(@RequestParam Long id,@RequestParam boolean active){
        ApiResponse response = sizeService.patch(id, active);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

}
