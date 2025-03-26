package com.linq.website.controller;

import com.linq.website.dto.ContactUsDTO;
import com.linq.website.dto.FormSubmissionDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.FormSubmission;
import com.linq.website.service.FormSubmissionService;
import com.linq.website.service.SlideContentService;
import com.linq.website.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        userService.sendContactUsEnquiryMail(dto);
        return ResponseEntity.ok(new SuccessResponse(true, "Form submitted successfully.",null));
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
}
