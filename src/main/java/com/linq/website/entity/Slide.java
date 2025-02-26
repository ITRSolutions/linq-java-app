package com.linq.website.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "slides")
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_block_id")
    private ContentBlock contentBlock;

    private String slideTitle;

    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    private List<SlideContent> slideContents;  // List to hold slide content

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentBlock getContentBlock() {
        return contentBlock;
    }

    public void setContentBlock(ContentBlock contentBlock) {
        this.contentBlock = contentBlock;
    }

    public String getSlideTitle() {
        return slideTitle;
    }

    public void setSlideTitle(String slideTitle) {
        this.slideTitle = slideTitle;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<SlideContent> getSlideContents() {
        return slideContents;
    }

    public void setSlideContents(List<SlideContent> slideContents) {
        this.slideContents = slideContents;
    }

    @Override
    public String toString() {
        return "Slide{" +
                "id=" + id +
                ", contentBlock=" + contentBlock +
                ", slideTitle='" + slideTitle + '\'' +
                ", orderIndex=" + orderIndex +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
