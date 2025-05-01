package com.linq.website.config;

import com.linq.website.filter.EmailIsVerifiedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //  Enables @PreAuthorize annotations
public class HttpSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailIsVerifiedFilter emailIsVerifiedFilter;

    @Value("${website.main.url}")
    private String websiteDomainName;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of(websiteDomainName));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))

                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Disable CSRF for API endpoints

//                .csrf(csrf -> csrf.disable()) //  CSRF disabled (but session authentication enforced)
//                .anonymous(anonymous -> anonymous.disable()) //  Completely disable anonymous access
                .anonymous(Customizer.withDefaults()) // Allow anonymous access to public pages
                .authorizeHttpRequests(auth -> auth
                        // Permit all static resources
                        .requestMatchers("/", "/{slug}", "/{slug}/**", "/error", "/css/**", "/js/**", "/image/**", "/font/**").permitAll()
                        .requestMatchers("/apply-job").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/api/v1/logs").permitAll() // Allow authentication APIs

                        //  Restrict ADMIN-ONLY APIs
                        .requestMatchers("/api/v1/users/**", "/api/v1/s3/**", "/api/v1/web_page/**",
                                "/api/v1/actuator/**", "/api/v1/content_block/**", "/api/v1/web_page/**",
                                "/api/v1/pageMetadata", "/api/v1/slideContent/**", "/api/v1/slide/**",
                                "/api/v1/users/**", "/actuator/**","/search-application","/get-job-applications").hasRole("ADMIN")

                        .anyRequest().authenticated() // Require authentication for any other request
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin_panel/", true)
                        .failureHandler(failureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin_panel/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // Ensures session is fully destroyed
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID") // Deletes session cookies
                        .permitAll()
                );

        http.exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                );
        http.addFilterAfter(emailIsVerifiedFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Spring Security automatically configures this in Spring Boot 6+
    }
}
