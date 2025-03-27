package com.linq.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linq.website.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "contactNumber", nullable = false)
    private String contactNumber;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "zipCode", nullable = false)
    private String zipCode;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "isEmailVerified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private RoleType role = RoleType.USER;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "password_reset_otp", nullable = true)
    @JsonIgnore
    private String passwordResetOtp;

    @Column(name = "password_reset_ref", nullable = true)
    @JsonIgnore
    private String passwordResetRef;

    @Column(name = "activateUser", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean activateUser = false;

    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
