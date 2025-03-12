package com.linq.website.repository;

import com.linq.website.entity.FormSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {

    List<FormSubmission> getByPageName(String pageName);
    List<FormSubmission> getByPageId(Long pageId);
}
