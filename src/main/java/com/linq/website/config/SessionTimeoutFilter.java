package com.linq.website.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SessionTimeoutFilter extends OncePerRequestFilter {

    // Define public paths (use Ant-style patterns)
    private static final List<String> PUBLIC_PATHS = List.of(
            "/", "/{slug}", "/{slug}/**", "/error", "/css/**", "/js/**", "/image/**", "/font/**",
            "/employee_registration", "/registration_form/**", "/error", "/error/**",
            "/api/v1/auth/**", "/api/v1/logs"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean isSessionExpired = (session == null || session.isNew());
        String path = request.getRequestURI();

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        System.out.println("isPublic: "+isPublic);
        System.out.println("isSessionExpired: "+isSessionExpired);
        if (isSessionExpired && !isPublic) {
            response.sendRedirect("/login?sessionExpired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
