package com.linq.website.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "dynamic_pages")
public class DynamicPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    @Enumerated(EnumType.STRING)
    private PageStatus status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

enum PageStatus {
    PUBLISHED,
    DRAFT
}

