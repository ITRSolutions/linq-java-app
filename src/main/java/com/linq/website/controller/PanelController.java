package com.linq.website.controller;

import com.linq.website.enums.PageStatus;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.MailService;
import com.linq.website.service.UserService;
import com.linq.website.utility.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class PanelController {

    @Autowired
    UserService userService;

    @Autowired
    DynamicPageService dynamicPageService;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(PanelController.class);

    @GetMapping({"/", "/{slug}"})
    public String getPage(@PathVariable(required = false) String slug, Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (slug == null || slug.isEmpty()) {
            slug = "dashboard.html";
        }

        try {
            // Check if userDetails is null (in case of session timeout)
            if (userDetails == null) {
                // Redirect or handle the session timeout scenario
                return "redirect:/login?sessionExpired";
            }
            boolean pageFound = templateExists("admin_panel/" + slug);
            System.out.println("templateExists: "+slug+" - "+pageFound);
            if (!pageFound) {
                return "redirect:/admin_panel/";
            }

            // Access the username of the logged-in user
            String firstName = userDetails.getFirstName();

            // Or, access more details via the Authentication object
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toString(); // Access roles

            // Add user details to the model to pass to Thymeleaf view
            model.addAttribute("firstName", firstName);
            model.addAttribute("role", role);
            long totalUserRegistered = userService.getTotalUserRegistered();
            long unverifiedUsers = userService.getTotalUnverifiedUser();
            long countPublishedStatus = dynamicPageService.getCountPageStatus(PageStatus.PUBLISHED);
            long countDraftStatus = dynamicPageService.getCountPageStatus(PageStatus.DRAFT);

            model.addAttribute("totalUsers",  totalUserRegistered);
            model.addAttribute("unverifiedUsers", unverifiedUsers);
            model.addAttribute("countPublishedStatus",  countPublishedStatus);
            model.addAttribute("countDraftStatus", countDraftStatus);

            System.out.println("firstName: " + firstName);
            System.out.println("role: " + role);
            return "admin_panel/" + slug;
        } catch (Exception ex) {
            return "redirect:/login?sessionExpired";
        }
    }

    private boolean templateExists(String templateName) {
        String templatePath = "classpath:/templates/" + templateName;
        Resource resource = resourceLoader.getResource(templatePath);
        return resource.exists();
    }
}
