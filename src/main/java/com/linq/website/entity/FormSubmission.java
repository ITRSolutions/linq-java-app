package com.linq.website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(nullable = true)
    private String dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = false)
    private String email;

    private String bestContactTime;

    private String zipCode;

    private String state;

    private String speciality;

    private Boolean isEmployee; // Yes/No -> true/false

    private Boolean isStudyParticipant; // Yes/No -> true/false

    private Boolean agreedToTerms;

    private String pageName;

    private Long pageId;

    private String comment;

    private String followBack;

    private String refName;

    private String refContactNumber;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
