package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseUrl.BASE_URL+"/homeUser")
@RequiredArgsConstructor
public class HomePageController {
//hasan
    private final HomeService homeService;

    @GetMapping("/mobile")
    public HttpEntity<?> getHomePage(@CurrentUser User user){
        ApiResponse apiResponse=homeService.homePage(user);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAdminElements(){
        ApiResponse response = homeService.getAdminElements();
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
}
