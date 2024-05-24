package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.DeliverDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/worker/web")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllDeliverer(){
        ApiResponse response = workerService.getAllDeliverer();
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")

    @GetMapping("/{id}")
    public HttpEntity<?> getByIdDeliverer (@PathVariable UUID id){
        ApiResponse response = workerService.getById(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public HttpEntity<?> addDeliverer(@RequestBody DeliverDto dto){
        ApiResponse apiResponse = workerService.addDeliverer(dto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDeliverer(@PathVariable UUID id){
        ApiResponse apiResponse = workerService.deleteDeliverer(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/{id}")
    public HttpEntity<?> enableOrDisable(@PathVariable UUID id, @RequestParam boolean active){
        ApiResponse apiResponse = workerService.enableOrDisabled(id, active);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_DELIVERER')")
    @GetMapping("/mobile/driver/new")
    public HttpEntity<?> getDriverOrders(@CurrentUser User user){
        ApiResponse response = workerService.getDriverOrders(user);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }



    @PreAuthorize(value = "hasRole('ROLE_DELIVERER')")
    @PatchMapping("/mobile/reject/deliver/{orderId}")
    public HttpEntity<?> rejectOrderByDriver(@PathVariable UUID orderId,@CurrentUser User user){
        ApiResponse response = workerService.rejectByDriver(orderId,user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_DELIVERER')")
    @GetMapping("/mobile/driver/old")
    public HttpEntity<?> findDriverOrders(@CurrentUser User user){
        ApiResponse response = workerService.findDriverOrders(user);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_DELIVERER')")
    @PatchMapping("/mobile/accept/{orderId}")
    public HttpEntity<?> acceptOrder(@PathVariable UUID orderId){
        ApiResponse response = workerService.accept(orderId);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }
}
