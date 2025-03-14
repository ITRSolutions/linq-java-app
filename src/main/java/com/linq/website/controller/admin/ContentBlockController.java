package com.linq.website.controller.admin;

import com.linq.website.dto.ContentBlockDTO;
import com.linq.website.dto.DynamicPageDTO;
import com.linq.website.dto.ErrorResponse;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.User;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.repository.ContentBlockRepository;
import com.linq.website.repository.DynamicPageRepository;
import com.linq.website.repository.UserRepository;
import com.linq.website.service.ContentBlockService;
import com.linq.website.service.DynamicPageService;
import com.linq.website.utility.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/content_block")
public class ContentBlockController {

    @Autowired
    private ContentBlockService contentBlockService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> createContentBlock(@Valid @RequestBody ContentBlockDTO.CreateContentBlock dto) {
        contentBlockService.createContentBlock(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "ContentBlock created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentBlock> updateContentBlock(@Valid @RequestBody ContentBlockDTO.UpdateContentBlock dto, @PathVariable Long id) {
        dto.setId(id); // Set the ID in case it's not in the path variable
        ContentBlock contentBlock = contentBlockService.updateContentBlock(dto);
        return ResponseEntity.ok(contentBlock);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getContentBlockByPageId(@PathVariable Long id) {
        List<ContentBlock> pageIdCBList = contentBlockService.getContentBlocksByPageId(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Content blocks.", pageIdCBList));
    }
}