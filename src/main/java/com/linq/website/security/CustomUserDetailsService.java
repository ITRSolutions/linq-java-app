package com.linq.website.security;

import com.linq.website.entity.User;
import com.linq.website.repository.UserRepository;
import com.linq.website.utility.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user by email using Optional
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new DisabledException("The entered email does not exist. Please Sign up."));

        if (!user.getIsEmailVerified()) {
            throw new DisabledException("Your email is not verified. A verification email has been sent to you.");
        } else if (!user.getActivateUser()) {
            throw new DisabledException("Your account is not verified by the admin. Please wait for verification.");
        }

        // Map the single RoleType to a SimpleGrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        //   Create and return a CustomUserDetails object with the token
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getEmail(),         // username (email)
                user.getPassword(),      // password
                user.getFirstName(),     // firstName
                user.getLastName(),      // lastName
                Collections.singletonList(authority)
        );

        return userDetails;
    }
}
