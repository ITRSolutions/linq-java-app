package com.linq.website.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePageNotFound(PageNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404"; // This will be an error page
    }
}