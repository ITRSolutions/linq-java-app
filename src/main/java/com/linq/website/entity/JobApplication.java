package com.linq.website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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

    private String resumeFileName;

    private String coverLetterFileName;

    private Boolean eligibleToWorkInUSA;

    private Boolean requiresVisaSponsorship;

    private Boolean residesInUSA;

    private String compensation;

    @Column(nullable = false)
    private String pageName;
}
