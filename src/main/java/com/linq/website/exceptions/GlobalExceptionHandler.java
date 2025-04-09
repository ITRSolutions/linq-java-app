package com.linq.website.exceptions;

import com.linq.website.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(assignableTypes = {
        com.linq.website.controller.WebController.class,
        com.linq.website.controller.PanelController.class
})
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle 404 error for Thymeleaf
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404Error(NoHandlerFoundException ex, Model model) {
        logger.error("NoHandlerFoundException : "+ex.getMessage());
        model.addAttribute("error", "The page you are looking for does not exist.");
        return "/error/404"; // View for 404 error
    }

    // Handle Page Not Found error for Thymeleaf
    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePageNotFound(PageNotFoundException ex, Model model) {
        logger.error("PageNotFoundException : "+ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "/error/404"; // View for PageNotFound error
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        logger.error("ResourceNotFoundException : "+ex);
        model.addAttribute("message", ex.getMessage());
        return "error/error_404"; // Custom error page for 404 errors
    }

    // Handle other exceptions for Thymeleaf
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("AccessDeniedException caught globally : "+ex);
        return "redirect:/login?sessionExpired"; //  Redirect works without @ResponseStatus
    }

    // Handle other exceptions for Thymeleaf
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        logger.error("Global Exception: "+ex);
        model.addAttribute("error", "Error: An unexpected error occurred.");
        return "/error/error";
    }
}
