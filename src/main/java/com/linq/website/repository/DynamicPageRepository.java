package com.linq.website.repository;

import com.linq.website.entity.DynamicPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DynamicPageRepository extends JpaRepository<DynamicPage, Long> {
    Optional<DynamicPage> findBySlug(String slug);

    Page<DynamicPage> findAll(Pageable pageable);
}
