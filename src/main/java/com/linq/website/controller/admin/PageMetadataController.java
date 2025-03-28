package com.linq.website.controller.admin;

import com.linq.website.dto.PageMetadataDto;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.CompanyPageMetaData;
import com.linq.website.entity.PageMetadata;
import com.linq.website.service.CompanyPageMetaDataService;
import com.linq.website.service.PageMetadataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pageMetadata")
@PreAuthorize("hasRole('ADMIN')") //  Ensures only ADMIN can access
public class PageMetadataController {

    @Autowired
    PageMetadataService pageMetadataService;

    @Autowired
    CompanyPageMetaDataService companyPageMetaDataService;

    @GetMapping()
    public ResponseEntity<?> getPageMetadata() {
        PageMetadata pageMetaData = pageMetadataService.getPageMetaData();
        return ResponseEntity.ok(new SuccessResponse<>(true, "PageMeta data info.", pageMetaData));
    }

    @PutMapping()
    public ResponseEntity<?> updatePageMetadata(@Valid @RequestBody PageMetadataDto dto) {
        pageMetadataService.updatePageMetaData(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Page Meta data updated successfully.", null));
    }

    @PutMapping("/company")
    public ResponseEntity<?> updatePageMetadata(@RequestBody CompanyPageMetaData dto) {
        companyPageMetaDataService.updateData(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Company Meta data updated successfully.", null));
    }

    @GetMapping("/company")
    public ResponseEntity<?> getCompanyMetadata() {
        CompanyPageMetaData data = companyPageMetaDataService.getData();
        return ResponseEntity.ok(new SuccessResponse<>(true, "Company meta data info.", data));
    }
}
