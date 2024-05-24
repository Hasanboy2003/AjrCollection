package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/addfiles")
    public HttpEntity<?> uploadFiles(MultipartHttpServletRequest request) throws IOException {
        ApiResponse apiResponse = attachmentService.addFiles(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIconById(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = attachmentService.getAttachmentById(id, response);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).build();
    }

    /**
     * The method which deletes icon.
     */
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteIconById(@PathVariable UUID id) {
        ApiResponse apiResponse = attachmentService.deleteAttachment(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }
}
