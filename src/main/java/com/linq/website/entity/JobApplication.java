package com.linq.website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String resumeURL;

    @Column(nullable = true)
    private String coverURL;

    @Column(nullable = false)
    private Boolean eligibleToWorkInUSA;

    @Column(nullable = false)
    private Boolean requiresVisaSponsorship;

    @Column(nullable = false)
    private Boolean residesInUSA;

    @Column(nullable = false)
    private String compensation;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String pageName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
