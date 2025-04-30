package com.linq.website.controller;

import com.linq.website.dto.ApplicationRequestDto;
import com.linq.website.dto.ContactUsDTO;
import com.linq.website.dto.FormSubmissionDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.FormSubmission;
import com.linq.website.entity.JobApplication;
import com.linq.website.service.FormSubmissionService;
import com.linq.website.service.S3Service;
import com.linq.website.service.SlideContentService;
import com.linq.website.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
public class FormSubmissionController {

    @Autowired
    private FormSubmissionService service;

    @Autowired
    private UserService userService;

    @Autowired
    private SlideContentService slideContentService;

    @Autowired
    private S3Service s3Service;

    @PermitAll
    @PostMapping()
    public ResponseEntity<SuccessResponse> submitForm( @RequestBody FormSubmissionDTO.Create dto) {
        service.saveSubmission(dto);
        return ResponseEntity.ok(new SuccessResponse(true, "Form submitted successfully.",null));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<SuccessResponse> getFormDataByPageName(@RequestParam String pageName,
                                                                 @RequestParam(defaultValue = "0") int page) {
        Page<FormSubmission> pageData = service.getFormDataByPageNamePagination(pageName, page);
        return ResponseEntity.ok(new SuccessResponse(true, "List of form according to page_name: "+pageName,pageData));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateForm(@PathVariable Long id,
                                                      @Valid @RequestBody FormSubmissionDTO.Update dto) {
        service.updateSubmission(dto, id);
        return ResponseEntity.ok(new SuccessResponse(true, "Form updated successfully.",null));
    }

    @PostMapping("/customer_enquiry")
    public ResponseEntity<SuccessResponse> sendContactEnquiry(@Valid @RequestBody ContactUsDTO dto) {
//        userService.sendContactUsEnquiryMail(dto);
        return ResponseEntity.ok(new SuccessResponse(true, "Form submitted successfully.",userService.sendContactUsEnquiryMail(dto)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchSubmissions(
            @RequestParam String searchString,
            @RequestParam String pageName) {
        List<FormSubmission> formList = service.searchSubmissionsByStringAndPageName(searchString, pageName);

        return ResponseEntity.ok(new SuccessResponse(true, "Search result of pageName: "+pageName,formList));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/diseases")
    public ResponseEntity<?> getDiseases() {
        List<String> diseasesList = slideContentService.getDiseasesList();
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Diseases.", diseasesList));
    }

    @PermitAll
    @PostMapping("/apply-job")
    public ResponseEntity<?> apply(@Valid @RequestBody ApplicationRequestDto dto) {
        System.out.println("Received application from: " + dto.getName());

        // Save resume & cover letter to S3
        try {
            String resumeURL = s3Service.uploadPdfFile(dto.getResume());
            dto.setResumeURL(resumeURL);
            System.out.println("resumeURL: "+resumeURL);
            if(dto.getCoverLetter() != null && !dto.getCoverLetter().getFileName().isEmpty()) {
                String coverURL = s3Service.uploadPdfFile(dto.getCoverLetter());
                dto.setCoverURL(coverURL);
                System.out.println("coverURL: "+coverURL);
            }

            service.submitJobApplicationForm(dto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new SuccessResponse(true, "Application submitted successfully.",null));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/search-application")
    public ResponseEntity<?> searchJobApplication(
            @RequestParam String searchString,
            @RequestParam String pageName) {
        List<JobApplication> formList = service.searchJobApplicationByKeyword(searchString, pageName);

        return ResponseEntity.ok(new SuccessResponse(true, "Search result of pageName: "+pageName,formList));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/get-job-applications")
    public ResponseEntity<SuccessResponse> getApplicationByPageName(@RequestParam String pageName,
                                                                 @RequestParam(defaultValue = "0") int page) {
        Page<JobApplication> pageData = service.getJobApplicationByPageNamePagination(pageName, page);
        return ResponseEntity.ok(new SuccessResponse(true, "List of job applications according to page_name: "+pageName,pageData));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/get-job-role")
    public ResponseEntity<?> getJobPositions() {
        List<String> jobPositions = service.getUniqueJobPositions();
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Job Positions.", jobPositions));
    }
}
