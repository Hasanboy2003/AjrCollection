package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.payload.AdvertisingDto;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.service.AdvertisingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(BaseUrl.BASE_URL+"/advertising")
public class AdvertisingController {

    private final AdvertisingService advertisingService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAllAdvertising(){
        ApiResponse apiResponse=advertisingService.getAllAdvertisingForAdmin();
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public HttpEntity<?> getByIdForAdmin(@PathVariable UUID id){
        ApiResponse apiResponse = advertisingService.getByIdForAdmin(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> addAdvertising(@RequestBody AdvertisingDto advertisingDto){
        ApiResponse apiResponse=advertisingService.addAdvertising(advertisingDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/web/{id}")
    public HttpEntity<?> editAdvertising (@PathVariable UUID id, @RequestBody AdvertisingDto advertisingDto){
        ApiResponse apiResponse=advertisingService.editAdvertising(id, advertisingDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> deleteAdvertising(@PathVariable UUID id){
        ApiResponse apiResponse=advertisingService.deleteAdvertising(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web/{id}")
    public HttpEntity<?> enabledOrDisabled(@PathVariable UUID id, @RequestParam boolean active){
        ApiResponse apiResponse=advertisingService.enableOrDisable(id, active);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/mobile")
    public HttpEntity<?> getAllForUser (){
        ApiResponse apiResponse=advertisingService.getAllForUser();
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


}
