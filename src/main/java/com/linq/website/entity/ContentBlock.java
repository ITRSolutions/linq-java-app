package com.linq.website.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "content_blocks")
public class ContentBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private DynamicPage page;

    private String content;

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
    private List<Slide> slide;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DynamicPage getPage() {
        return page;
    }

    public void setPage(DynamicPage page) {
        this.page = page;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<Slide> getSlide() {
        return slide;
    }

    public void setSlide(List<Slide> slide) {
        this.slide = slide;
    }

    @Override
    public String toString() {
        return "ContentBlock{" +
                "id=" + id +
                ", page=" + page +
                ", content='" + content + '\'' +
                ", orderIndex=" + orderIndex +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", slide=" + slide +
                '}';
    }
}