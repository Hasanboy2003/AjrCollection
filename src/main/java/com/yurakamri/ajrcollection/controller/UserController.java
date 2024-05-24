package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.UserDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(BaseUrl.BASE_URL+"/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/web")
    public HttpEntity<?> getAllUserByAdmin(){
        ApiResponse allUserForAdmin = userService.getAllUserForAdmin();
        return ResponseEntity.status(allUserForAdmin.isSuccess()?200:409).body(allUserForAdmin);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/web/{id}")
    public HttpEntity<?> deleteUserForAdmin(@PathVariable UUID id, @CurrentUser User user){
        ApiResponse apiResponse=userService.deleteUserForAdmin(id, user);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/web/{id}")
    public HttpEntity<?> enableOrDisable(@PathVariable UUID id, @RequestParam boolean active){
        ApiResponse apiResponse=userService.enableOrDisable(id, active);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping("/mobile/{id}")
    public HttpEntity<?> deleteUserForUser(@PathVariable UUID id){
        ApiResponse apiResponse=userService.deleteUserForUser(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_DELIVERER')")
    @PostMapping("/mobile/addAttachment")
    public HttpEntity<?> addAttachmentForUser(@CurrentUser User user, @RequestParam UUID attachmentId){
        ApiResponse apiResponse=userService.addAttachmentByUser(user,attachmentId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PatchMapping("/mobile/favourite/{id}")
    public HttpEntity<?> addFavourites(@PathVariable UUID id,@CurrentUser User user){
        ApiResponse response = userService.addFavourites(id,user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }



    @GetMapping("/mobile/favourite")
    public HttpEntity<?> getFavourite(@CurrentUser User user){
        ApiResponse response = userService.getFavourites(user);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_DELIVERER')")
    @DeleteMapping("/mobile/attachment")
    public HttpEntity<?> deleteAttachment (@CurrentUser User user){
        ApiResponse response=userService.deleteAttachment(user);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_DELIVERER')")
    @GetMapping(BaseUrl.M + "/profile")
    public ResponseEntity<ApiResponse> getProfile(@CurrentUser User user) {
        UserDto profile = userService.generateUserDto(user);
        return ResponseEntity.ok(new ApiResponse(true, null, profile));
    }
}
