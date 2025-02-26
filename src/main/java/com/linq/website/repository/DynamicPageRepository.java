package com.linq.website.repository;

import com.linq.website.entity.DynamicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicPageRepository extends JpaRepository<DynamicPage, Long> {
    DynamicPage findBySlug(String slug);
}
