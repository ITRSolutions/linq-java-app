package com.linq.website.controller;

import com.linq.website.utility.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin_panel/")
public class PanelController {

    @GetMapping("/{slug}")
    public String getPage(@PathVariable String slug, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Access the username of the logged-in user
        String firstName = userDetails.getFirstName();

        // Or, access more details via the Authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString(); // Access roles

        // Add user details to the model to pass to Thymeleaf view
        model.addAttribute("firstName", firstName);
        model.addAttribute("role", role);

        System.out.println("firstName: " + firstName);
        System.out.println("role: " + role);
        return "admin_panel/" + slug;

    }
}
