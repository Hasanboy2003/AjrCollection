package com.yurakamri.ajrcollection.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {

    boolean success;
    String message;
    Object object;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Object object) {
        this.object = object;
        this.success = true;
    }

    public static ResponseEntity<ApiResponse> response(HttpStatus status, Object body) {
        return ResponseEntity.status(status).body(new ApiResponse(body));
    }

    public static ResponseEntity<ApiResponse> response(Object body) {
        return ResponseEntity.ok(new ApiResponse(body));
    }

    public static ResponseEntity<ApiResponse> response(boolean success, String message) {
        return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.CONFLICT).body(new ApiResponse(success, message));
    }

    public static ResponseEntity<ApiResponse> response(boolean success, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse(success, message));
    }

    public static ResponseEntity<ApiResponse> response(String message, Object body) {
        return ResponseEntity.ok(new ApiResponse(true, message, body));
    }
}
