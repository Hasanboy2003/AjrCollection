package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.ChangeProfile;
import com.yurakamri.ajrcollection.payload.req.auth.LoginDto;
import com.yurakamri.ajrcollection.payload.req.auth.RegisterDto;
import com.yurakamri.ajrcollection.payload.req.auth.VerificationDto;
import com.yurakamri.ajrcollection.security.CurrentUser;
import com.yurakamri.ajrcollection.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(BaseUrl.BASE_URL + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * REGISTRATION PROCESS AFTER WHICH IF USER DOES NOT ALREADY EXIST AND PHONE NUMBER EXISTS AT ALL THEN THE USER
     * WILL RECEIVE A VERIFICATION CODE IN ORDER TO VERIFY THE ACCOUNT
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login/driver")
    public HttpEntity<?> loginDriver(@RequestBody LoginDto loginDto){
        return  authService.loginDriver(loginDto);
    }

    /**
     * THE PROCESS OF ACCOUNT VERIFYING AFTER REGISTRATION
     */
    @PostMapping("/verifyAccount")
    public ResponseEntity<ApiResponse> verifyAccount(@RequestBody VerificationDto dto) {
        return authService.verifyAccount(dto);
    }

    /**
     * THE PROCESS OF LOG IN IF USER IS ALREADY IN DB AND HIS ACCOUNT IS VERIFIED THEN HE WILL RECEIVE VERIFICATION CODE
     * IN ORDER TO LOG IN THE SYSTEM AGAIN
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }

    /**
     * PHONE NUMBER VERIFICATION AFTER THE PROCESS OF LOGIN, IF NOT VERIFIED IN 2 MINUTES THEN THE CODE WILL BE SET TO
     * NULL
     */
    @PostMapping("/verifyPhoneNumber")
    public ResponseEntity<ApiResponse> verifyPhoneNumber(@RequestBody VerificationDto dto) {
        return authService.verifyPhoneNumber(dto);
    }

    /**
     * RESEND ACCOUNT VERIFICATION CODE
     */
    @PostMapping("/resendAccountVerificationCode")
    public ResponseEntity<ApiResponse> resendAccountVerificationCode(@RequestBody LoginDto dto) {
        return authService.resendAccountVerificationCode(dto);
    }

    /**
     * RESEND PHONE NUMBER VERIFICATION CODE
     */
    @PostMapping("/resendPhoneNumberVerificationCode")
    public ResponseEntity<ApiResponse> resendPhoneNumberVerificationCode(@RequestBody LoginDto dto) {
        return authService.resendPhoneNumberVerificationCode(dto);
    }

    @PostMapping("/changePhoneNumber")
    public ResponseEntity<ApiResponse> changePhoneNumber(@RequestBody ChangeProfile dto,
                                                         @CurrentUser User user) {
        return authService.changePhoneNumber(dto, user);
    }

    /**
     * TEMPORAL PHONE NUMBER VERIFICATION TO CHANGE USER'S CURRENT PHONE NUMBER
     */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DELIVERER')")
    @PostMapping("/verifyPhoneNumberChange")
    public ResponseEntity<ApiResponse> verifyPhoneNumberChange(@RequestBody VerificationDto dto,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        return authService.verifyPhoneNumberChange(dto, userDetails);
    }

    /**
     * IF HE DOES NOT GET VERIFICATION CODE THEN AFTER 2 MINUTES HE CAN RECEIVE IT AGAIN
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/resendPhoneNumberChangeCode")
    public ResponseEntity<ApiResponse> resendPhoneNumberChangeCode(@RequestBody LoginDto dto,
                                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return authService.resendPhoneNumberChangeCode(dto, userDetails);
    }
}
