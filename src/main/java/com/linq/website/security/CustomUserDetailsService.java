package com.linq.website.security;

import com.linq.website.entity.User;
import com.linq.website.repository.UserRepository;
import com.linq.website.utility.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Map the single RoleType to a SimpleGrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // Return the user details with the authorities (roles)
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),    // username
//                user.getPassword(), // password
//                Collections.singletonList(authority) // single role in a list
//        );
        // Create and return a CustomUserDetails object
        return new CustomUserDetails(
                user.getEmail(),          // username (email)
                user.getPassword(),       // password
                user.getFirstName(),      // firstName
                user.getLastName(),       // lastName
                Collections.singletonList(authority) // role (authorities)
        );
    }

}
