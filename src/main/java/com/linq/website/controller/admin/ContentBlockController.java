package com.linq.website.controller.admin;

import com.linq.website.dto.ContentBlockDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.ContentBlock;
import com.linq.website.service.ContentBlockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content_block")
@PreAuthorize("hasRole('ADMIN')") //  Ensures only ADMIN can access
public class ContentBlockController {

    @Autowired
    private ContentBlockService contentBlockService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> createContentBlock(@Valid @RequestBody ContentBlockDTO.CreateContentBlock dto) {
        contentBlockService.createContentBlock(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "ContentBlock created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateContentBlock(@Valid @RequestBody ContentBlockDTO.UpdateContentBlock dto, @PathVariable Long id) {
        dto.setId(id); // Set the ID in case it's not in the path variable
        ContentBlock contentBlock = contentBlockService.updateContentBlock(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "ContentBlock updated successfully.", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getContentBlockByPageId(@PathVariable Long id) {
        List<ContentBlock> pageIdCBList = contentBlockService.getContentBlocksByPageId(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Content blocks.", pageIdCBList));
    }

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Access forbidden");
    }
}