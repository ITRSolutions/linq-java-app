package com.linq.website.dto;

import com.linq.website.enums.RoleType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDTO {
    @Getter
    @Setter
    public static class User {
        private Long id;
        private String firstName;
        private String lastName;
        private String contactNumber;
        private String gender;
        private String dob;
        private String city;
        private String state;
        private String country;
        private String zipCode;
        private String email;
        private RoleType role;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

//    @Getter
//    @Setter
//    public static class LoginUserRequest {
//        @NotNull(message = "Email address is required.")
//        @NotBlank(message = "Email address is required.")
//        @Email(message = "Email address should be valid.")
//        private String email;
//
//        @NotNull(message = "Password is required.")
//        @Size(min = 8, message = "Password must be at least 8 characters long.")
//        private String password;
//    }

    @Getter
    @Setter
    public static class CreateUser {
        @NotBlank(message = "First name is required.")
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last name is required.")
        private String lastName;

        @NotBlank(message = "Contact number is required.")
        //@Pattern(regexp = "^(\\+1\\s?)?\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{4}$", message = "Contact number must be a valid US phone number.")
        private String contactNumber;

        @NotBlank(message = "Gender is required.")
        private String gender;

        @NotBlank(message = "Date of Birth is required.")
        private String dob;

        @NotBlank(message = "City is required.")
        private String city;
//
        @NotBlank(message = "State is required.")
        private String state;
//
        @NotBlank(message = "Country is required.")
        private String country;
//
        @NotBlank(message = "ZIP code is required.")
        private String zipCode;

        @NotBlank(message = "Email address is required.")
        @Email(message = "Email address should be valid.")
        private String email;

        @NotBlank(message = "Password is required.")
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        private String password;

        @NotBlank(message = "Confirm password is required.")
        @Size(min = 8, message = "Confirm password must be at least 8 characters long.")
        private String confirmPassword;
    }

    @Getter
    @Setter
    public static class UpdateUser {
        @NotBlank(message = "First name is required.")
        private String firstName;

        @NotBlank(message = "Last name is required.")
        private String lastName;

        @NotBlank(message = "Contact number is required.")
        //@Pattern(regexp = "^(\\+1\\s?)?\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{4}$", message = "Contact number must be a valid US phone number.")
        private String contactNumber;

        @NotBlank(message = "Date of Birth is required.")
        private String dob;

        @NotBlank(message = "Gender is required.")
        private String gender;

        @NotBlank(message = "City is required.")
        private String city;

        @NotBlank(message = "State is required.")
        private String state;

        @NotBlank(message = "Country is required.")
        private String country;

        @NotBlank(message = "ZIP code is required.")
        private String zipCode;
    }

    @Getter
    @Setter
    public static class ChangePassword {
        @NotBlank(message = "Old password is required.")
        @Size(min = 8, message = "Old password must be at least 8 characters long.")
        private String oldPassword;

        @NotBlank(message = "New password is required.")
        @Size(min = 8, message = "New password must be at least 8 characters long.")
        private String newPassword;

        @NotBlank(message = "Confirm password is required.")
        @Size(min = 8, message = "Confirm password must be at least 8 characters long.")
        private String confirmPassword;
    }

    @Getter
    @Setter
    public static class ForgetPassword {
        @NotNull(message = "Email address is required.")
        @NotBlank(message = "Email address is required.")
        @Email(message = "Email address should be valid.")
        private String email;
    }

    @Getter
    @Setter
    public static class VerifyOTPCode extends ForgetPassword {
        @NotBlank(message = "4 digit OTP code is required.")
        @Size(min = 4, message = "OTP code must be 4 digit.")
        @Size(max = 4, message = "OTP code must be 4 digit.")
        private String otpCode;
    }

    @Getter
    @Setter
    public static class SetPassword {
        @NotNull(message = "Reference is required.")
        @NotBlank(message = "Reference is required.")
        private String ref;

        @NotBlank(message = "New password is required.")
        @Size(min = 8, message = "New password must be at least 8 characters long.")
        private String password;

        @NotBlank(message = "Confirm password is required.")
        @Size(min = 8, message = "Confirm password must be at least 8 characters long.")
        private String confirmPassword;
    }
}
