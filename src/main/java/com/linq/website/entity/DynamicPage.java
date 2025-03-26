package com.linq.website.entity;

import com.linq.website.enums.PageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // @PreUpdate method to update the timestamp before update
//    @PreUpdate
//    public void preUpdate() {
//        // Manually set updated_at to the current timestamp
//        this.updatedAt = LocalDateTime.now();
//    }
}

