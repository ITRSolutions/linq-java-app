package com.linq.website.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;

@Controller
//@PermitAll
public class CustomErrorController implements ErrorController {

    private static final String YEAR = "year";
    private static final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    @GetMapping("/error_404")
    public String handleError404(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("statusCode", statusCode != null ? statusCode : "Unknown");
        model.addAttribute("message", message != null ? message : "Something went wrong.");

        System.out.println("Error page access: "+currentYear);
        model.addAttribute(YEAR, currentYear);
        return "error/error_404"; // This will redirect to the 404 page
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("statusCode", statusCode != null ? statusCode : "Unknown");
        model.addAttribute("message", message != null ? message : "Something went wrong.");

        return "error/error"; // Ensure this template exists and renders safely
    }
}
