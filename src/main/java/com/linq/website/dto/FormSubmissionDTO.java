package com.linq.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSubmissionDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String bestContactTime;
    private String zipCode;
    private String state;
    private String speciality;

    private Boolean isEmployee;
    private Boolean isStudyParticipant;

    private Boolean agreedToTerms;
}
