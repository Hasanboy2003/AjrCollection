package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.DistributorDto;
import com.yurakamri.ajrcollection.service.DistributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(BaseUrl.BASE_URL_WEB + "/distributors")
@RequiredArgsConstructor
public class DistributorController {

    private final DistributorService distributorService;

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getDistributors(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page) throws PageSizeException {
        return distributorService.getDistributors(page);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return distributorService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> addDistributor(@Valid @RequestBody DistributorDto dto) {
        return distributorService.addDistributor(dto);
    }

    @PatchMapping
    public HttpEntity<?> patch(@RequestParam Long id, @RequestParam boolean active){
        return distributorService.patch(id, active);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDistributor(@PathVariable Long id) {
        return distributorService.deleteDistributor(id);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDistributor(@PathVariable Long id, @RequestBody DistributorDto dto) {
        return distributorService.updateDistributor(id, dto);
    }
}
