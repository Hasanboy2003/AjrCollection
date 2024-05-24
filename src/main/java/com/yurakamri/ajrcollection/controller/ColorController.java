package com.yurakamri.ajrcollection.controller;


import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.ColorDto;
import com.yurakamri.ajrcollection.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseUrl.BASE_URL+"/color")

public class ColorController {

    @Autowired
    ColorService colorService;


    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getColors() {
        ApiResponse response = colorService.getColors();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/mobile")
    public HttpEntity<?> findColors() {
        ApiResponse response = colorService.getColors();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web/{id}")
    public HttpEntity<?> getById(@PathVariable Long id) {
        ApiResponse response = colorService.getById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/mobile/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ApiResponse response = colorService.getById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResponse response = colorService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/web")
    public HttpEntity<?> update(@RequestBody ColorDto colorDto) {
        ApiResponse response = colorService.update(colorDto);
        return ResponseEntity.status    (response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/web")
    public HttpEntity<?> create(@RequestBody ColorDto dto) {
        ApiResponse response = colorService.create(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web")
    public HttpEntity<?> patch(@RequestParam Long id,@RequestParam boolean active){
        ApiResponse response = colorService.patch(id, active);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

}
