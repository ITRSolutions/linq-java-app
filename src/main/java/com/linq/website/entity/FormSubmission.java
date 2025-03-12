package com.linq.website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "form_submissions")
public class FormSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String bestContactTime; // Comma-separated values

    private String zipCode;

    private String state;

    private String speciality; // Applicable for form 2

    private Boolean isEmployee; // Yes/No -> true/false

    private Boolean isStudyParticipant; // Yes/No -> true/false

    private Boolean agreedToTerms;

    private String pageName;

    private Long pageId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
