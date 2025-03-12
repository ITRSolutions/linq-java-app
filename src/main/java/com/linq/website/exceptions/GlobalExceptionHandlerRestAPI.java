package com.linq.website.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerRestAPI {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("message", "Validation error.");

        // Create a map to store field errors
        Map<String, List<String>> errors = new HashMap<>();

        // Collect each field's error messages
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            // Add the error message as an array in the errors map
            errors.computeIfAbsent(fieldName, key -> new java.util.ArrayList<>()).add(errorMessage);
        });

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("message", "An unexpected error occurred.");

        // Create a map to store generic errors
        Map<String, List<String>> errors = new HashMap<>();
        errors.computeIfAbsent("server", key -> new java.util.ArrayList<>()).add("Something went wrong!");

        response.put("errors", errors);
        System.out.println("errors: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle PageNotFoundException
    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePageNotFoundException(PageNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("message", "An unexpected error occurred.");

        // Create a map to store generic errors
        Map<String, List<String>> errors = new HashMap<>();
        errors.computeIfAbsent("server", key -> new java.util.ArrayList<>()).add("Something went wrong!");

        response.put("errors", errors);
        System.out.println("errors: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("message", "An unexpected error occurred.");

        // Create a map to store generic errors
        Map<String, List<String>> errors = new HashMap<>();
        errors.computeIfAbsent("server", key -> new java.util.ArrayList<>()).add("Something went wrong!");

        response.put("errors", errors);
        System.out.println("errors: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
