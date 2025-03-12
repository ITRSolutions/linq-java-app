package com.linq.website.filter;

import com.linq.website.entity.User;
import com.linq.website.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
public class EmailIsVerifiedFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip email verification for public pages
        if (path.startsWith("/css/") || path.startsWith("/js/")
                || path.startsWith("/font/") || path.startsWith("/image/") || path.startsWith("/mail/")) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            // Assume userDetails is a custom user class that has an "isEmailVerified()" method
            if (userDetails instanceof User user) {
                if (!user.getIsEmailVerified()) {
                    response.sendRedirect("login?error=emailNotVerified");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/public");
    }
}


