package com.linq.website.controller;

import com.linq.website.dto.ErrorResponse;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.dto.UserDTO;
import com.linq.website.entity.User;
import com.linq.website.enums.RoleType;
import com.linq.website.service.MailService;
import com.linq.website.service.UserService;
import com.linq.website.utility.Helpers;
import com.linq.website.utility.LoggedUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Value("${website.main.url}")
    private String websiteDomainName;

    @Autowired
    LoggedUser loggedUser;

    // Register an account
    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO.CreateUser request) {

        // Match confirm password
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("confirmPassword", new String[]{"Confirm password isn't matched with password."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Data mismatch.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check existing email
        Optional<User> availableUserByEmail = userService.findByEmail(request.getEmail());
        if (availableUserByEmail.isPresent()) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("email", new String[]{"Email already in use. Please enter another or login."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Found existing data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Check existing contact number
        Optional<User> availableUserByContactNumber = userService.findByContactNumber(request.getContactNumber());
        if (availableUserByContactNumber.isPresent()) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("contactNumber", new String[]{"Contact already in use. Please enter another Contact."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Found existing data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Generated hash password
        String hashPassword = Helpers.generateEncryptedPassword(request.getPassword());

        userService.saveUser(request, hashPassword, RoleType.USER);

        return ResponseEntity.ok(new SuccessResponse<>(true, "Account created successfully.", null));
    }

    // Active email address
    @GetMapping("activate")
    public RedirectView activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (user.isEmpty()) {
            return new RedirectView("/activationError?message=Oops%20something%20went%20wrong.%20Contact%20Admin.", true);
        }

        return new RedirectView("/login?accountActive", true); // true makes it a redirect to the login page
    }

    // Forgot password request
    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody UserDTO.ForgetPassword request) {
        // Check existing account using email
        Optional<User> availableAccount = userService.findByEmail(request.getEmail());
        if (availableAccount.isEmpty()) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("account", new String[]{"Account not found."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("invalid data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        Optional<User> candidate = userService.findByEmail(request.getEmail()).map(user -> {
            user.setPasswordResetOtp(String.valueOf(randomNumber));
            return user;
        });

        if (candidate.isPresent()) {
            userService.updatePasswordResetOtp(candidate.get().getId(), candidate.get().getPasswordResetOtp());
            mailService.sendPasswordResetMail(candidate.orElseThrow());
        }

        return ResponseEntity.ok(new SuccessResponse<>(true, "The OTP code has been sent to your email.", null));
    }

    // Verify OTP code
    @PostMapping("verify-otp-code")
    public ResponseEntity<?> verifyOTPCode(@Valid @RequestBody UserDTO.VerifyOTPCode request) {

        // Check existing account using email & OTP code
        Optional<User> availableAccount = userService.findByEmailAndOtpCode(request.getEmail(), request.getOtpCode());
        if (availableAccount.isEmpty()) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("credentials", new String[]{"Invalid email address or OTP code."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("invalid data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Generate a secure string for password reset reference
        String refSecureString = Helpers.generateSecureString();

        // Update password reset ref
        userService.updatePasswordResetRef(availableAccount.get().getId(), refSecureString);

        Map<String, String> message = new HashMap<>();
        message.put("ref", refSecureString);

        return ResponseEntity.ok(new SuccessResponse<>(true, "The OTP code has been successfully verified.", message));
    }

    // Set new password
    @PutMapping("set-password")
    public ResponseEntity<?> setPassword(@Valid @RequestBody UserDTO.SetPassword request) {

        // Match new password & confirm password
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("confirmPassword", new String[]{"Confirm password isn't matched with password."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Data mismatch.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Generate hash password
        String hashPassword = Helpers.generateEncryptedPassword(request.getPassword());

        // Update new password & make password reset ref null
        userService.updatePassword(loggedUser.getUpdatedByUserObj().getId(), hashPassword);

        return ResponseEntity.ok(new SuccessResponse<>(true, "Password updated successfully.", null));
    }

}
