package com.linq.website.repository;

import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.FormSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {

    List<FormSubmission> getByPageName(String pageName);

    List<FormSubmission> getByPageId(Long pageId);

    Page<FormSubmission> findByPageName(String pageName, Pageable pageable);

    @Query("SELECT f FROM FormSubmission f WHERE " +
            "(LOWER(f.pageName) = LOWER(:pageName)) AND " +
            "(LOWER(f.firstName) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(f.lastName) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(f.email) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(f.phoneNumber) LIKE LOWER(CONCAT('%', :searchString, '%')))")
    List<FormSubmission> searchByStringAndPageName(String searchString, String pageName);

}
