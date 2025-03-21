package com.linq.website.config;

import com.linq.website.filter.EmailIsVerifiedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailIsVerifiedFilter emailIsVerifiedFilter;

    @Value("${website.main.url}")
    private String websiteDomainName;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        //From Spring6 no need to set user details ,
        // it will automatically set user details, service and password encoded objects to auntication.
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
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Disable CSRF only for APIs
                .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/{slug}", "/{slug}/**", "/error", "/css/**", "/js/**", "/image/**", "/font/**").permitAll()
                        .requestMatchers("/employee_registration", "/registration_form/**").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/web_page/**", "/api/v1/content_block/**", "/api/v1/slide/**",
                                        "/api/v1/slideContent/**","/api/v1/s3/**","/api/v1/users/**").permitAll()
                        .requestMatchers("/admin_panel/**").hasRole("USER")
                        .requestMatchers("/api/v1/admin/**", "/admin_panel/**").hasRole("ADMIN") // Fixed admin access
//                        .requestMatchers("/api/v1/web_page/**", "/admin_panel/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin_panel/", true)
                        .failureHandler(failureHandler()) // Register failure handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin_panel/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?sessionExpired") // Handle session expiration
                        .sessionFixation().newSession()
                )
                .addFilterAfter(emailIsVerifiedFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
        return new CustomAuthenticationFailureHandler();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());      //Just passwordEncoder bean and UserDetailsService bean must be there then no need of this configuration setting as sping6 will automatically set user details, service and password encoded objects to auntication.
    }
}