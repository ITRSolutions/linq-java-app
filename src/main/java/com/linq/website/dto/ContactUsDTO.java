package com.linq.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsDTO {
    @NotEmpty(message = "First Name is required")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters")
    private String firstName;

    @NotEmpty(message = "Last Name is required")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Size(min = 10, max = 20, message = "Phone number should be between 10 and 15 characters")
    private String phone;

    @NotEmpty(message = "Message is required")
    @Size(min = 10, max = 500, message = "Message must be between 10 and 500 characters")
    private String message;

}
