package com.linq.website.repository;

import com.linq.website.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByPageName(String pageName, Pageable pageable);

    @Query("SELECT j FROM JobApplication j WHERE j.pageName = :pageName AND " +
            "(LOWER(j.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<JobApplication> searchByPageNameAndKeyword(
            @Param("pageName") String pageName,
            @Param("keyword") String keyword
    );

    @Query("SELECT DISTINCT LOWER(j.jobTitle) FROM JobApplication j")
    List<String> findDistinctJobTitlesIgnoreCase();
}
