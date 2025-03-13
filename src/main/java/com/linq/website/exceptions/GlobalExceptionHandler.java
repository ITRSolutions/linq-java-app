package com.linq.website.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle 404 error for Thymeleaf
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404Error(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", "The page you are looking for does not exist.");
        return "error/404"; // View for 404 error
    }

    // Handle Page Not Found error for Thymeleaf
    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePageNotFound(PageNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404"; // View for PageNotFound error
    }

    // Handle other exceptions for Thymeleaf
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred.");
        return "error/404"; // View for general error page
    }
}
