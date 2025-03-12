package com.linq.website.controller;

import com.linq.website.dto.ContactUsDTO;
import com.linq.website.dto.FormSubmissionDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.FormSubmission;
import com.linq.website.service.FormSubmissionService;
import com.linq.website.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
public class FormSubmissionController {

    @Autowired
    private FormSubmissionService service;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> submitForm(@Valid @RequestBody FormSubmissionDTO dto) {
        service.saveSubmission(dto);
        return ResponseEntity.ok(new SuccessResponse(true, "Form submitted successfully.",null));
    }

    @GetMapping("/page_name/{page_name}")
    public ResponseEntity<SuccessResponse> getFormDataByPageName(@PathVariable String page_name) {
        List<FormSubmission> formDataList = service.getFormDataByPageName(page_name);
        return ResponseEntity.ok(new SuccessResponse(true, "List of form according to page_name",formDataList));
    }

    @GetMapping("/page_id/{page_id}")
    public ResponseEntity<SuccessResponse> getFormDataByPageId(@PathVariable Long page_id) {
        List<FormSubmission> formDataList = service.getFormDataByPageId(page_id);
        return ResponseEntity.ok(new SuccessResponse(true, "List of form according to page_id",formDataList));
    }

    @PostMapping("/customer_enquiry")
    public ResponseEntity<SuccessResponse> sendContactEnquiry(@Valid @RequestBody ContactUsDTO dto) {
        userService.sendContactUsEnquiryMail(dto);
        return ResponseEntity.ok(new SuccessResponse(true, "Form submitted successfully.",null));
    }
}
