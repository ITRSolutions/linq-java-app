package com.linq.website.repository;

import com.linq.website.entity.DynamicPage;
import com.linq.website.enums.PageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicPageRepository extends JpaRepository<DynamicPage, Long> {
    Optional<DynamicPage> findBySlugAndStatus(String slug, PageStatus status);

    Optional<DynamicPage> findBySlug(String slug);

    Page<DynamicPage> findAll(Pageable pageable);

    // Dynamic search based on the parameters title, slug, and status
    @Query("SELECT dp FROM DynamicPage dp WHERE " +
            "(:slug IS NULL OR dp.slug LIKE %:slug%)")
    List<DynamicPage> search(@Param("slug") String slug);
}
