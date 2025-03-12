package com.linq.website.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class Helpers {
    // Generate unique username
    public static String generateUniqueUsername(String firstName, String lastName) {
        long currentTimeInMills = System.currentTimeMillis();
        String sanitizedFirstName = firstName.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
        String sanitizedLastName = lastName.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();

        return sanitizedFirstName + "-" + sanitizedLastName + "-" + currentTimeInMills;
    }

    // Generate encrypted hash password
    public static String generateEncryptedPassword(String plainPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainPassword);
    }

    // Verify plain password with hash password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    // Pagination response
    @Setter
    @Getter
    public static class PaginationResponsePattern<T> {
        private Long total;
        private List<T> data;

        // Constructor
        public PaginationResponsePattern(Long total, List<T> data) {
            this.total = total;
            this.data = data;
        }
    }

    // Get base URL of application
    public static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        // Build the base URL
        return scheme + "://" + serverName + (serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort);
    }

    // Generate a random encrypted secure string
    public static String generateSecureString() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        // Encode the bytes to a Base64 string for readability
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}

