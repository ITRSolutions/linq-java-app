package com.linq.website.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "slide_contents")
public class SlideContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slide_id")
    private Slide slide;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(length = 2000)
    private String content;

    private String customCss;

    private String imageAltText;

    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}

enum ContentType {
    TEXT,
    IMAGE,
    BUTTON,
    URL,
    DISEASE_NAME
}
