package com.linq.website.controller;

import com.linq.website.dto.ErrorResponse;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/s3")
public class BucketController {

    @Autowired
    private S3Service s3Service;

    @GetMapping
    public ResponseEntity<String> getFileInfo() {
        // For example, you could return some info about the S3 storage
        return ResponseEntity.ok("S3 API endpoint is working.");
    }

    /**
     * Endpoint to upload a file to S3
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String[]> errors = new HashMap<>();
        // Check if the file size is greater than 5MB (5 * 1024 * 1024 bytes)
        if (file.isEmpty()) {
            errors.put("Image", new String[]{"No file uploaded."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("No file uploaded.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (file.getSize() > 3 * 1024 * 1024) {
            errors.put("Image", new String[]{"File size exceeds the 3MB limit."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Image size exceeds.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            // Upload the file to S3
            String fileUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok(new SuccessResponse<>(true, "File uploaded successfully.", fileUrl));
        } catch (IOException e) {
            errors.put("Image", new String[]{"Error uploading file."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Invalid file.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to retrieve all file paths (S3 URLs) from the S3 bucket
     */
    @GetMapping("/get-all-files")
    public ResponseEntity<?> getAllFilePaths() {
        List<String> allFilePaths = s3Service.getAllFilePaths();
        return ResponseEntity.ok(new SuccessResponse<>(true, "Image file list", allFilePaths));
    }
}