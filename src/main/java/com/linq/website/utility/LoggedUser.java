package com.linq.website.utility;

import com.linq.website.entity.User;
import com.linq.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;

@Component
public class LoggedUser {

    @Autowired
    private UserRepository userRepository;

    public User getUpdatedByUserObj() {
        try{
            // Fetch the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            // Get the User object using the username
            return userRepository.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new RuntimeException("User not found. Please re-login"));
        } catch (RuntimeException ex) {
            System.out.println("User login Exception: "+ex);
            return null;
        }
    }
}
