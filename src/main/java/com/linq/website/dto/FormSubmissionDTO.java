package com.linq.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSubmissionDTO {

    @Getter
    @Setter
    public static class Create {
        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

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
        private String pageName;
        private Long pageId;
        private Boolean agreedToTerms;
        private String refName;
        private String refContactNumber;

    }

    @Getter
    @Setter
    public static class Update {

        @NotBlank(message = "Comment is required")
        private String comment;
        @NotBlank(message = "followBack is required")
        private String followBack;

    }

}
