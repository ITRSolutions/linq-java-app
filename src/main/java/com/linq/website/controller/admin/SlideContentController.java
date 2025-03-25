package com.linq.website.controller.admin;

import com.linq.website.dto.SlideContentDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.SlideContent;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.SlideContentService;
import com.linq.website.utility.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slideContent")
public class SlideContentController {

    @Autowired
    private SlideContentService slideContentService;

    @Autowired
    private DynamicPageService dynamicPageService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> createSlideContent(@Valid @RequestBody SlideContentDTO.Create dto) {
        slideContentService.createSlideContent(dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide Content created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateSlideContent(@Valid @RequestBody SlideContentDTO.Update dto, @PathVariable Long id) {
        slideContentService.updateSlide(dto,id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide Content updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteSlideContent(@PathVariable Long id) {
        slideContentService.deleteSlideContent(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide Content is deleted successfully id: "+id, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllSlideContentById(@RequestParam(defaultValue = "0") int page, @PathVariable Long id) {
        List<SlideContent> slideContents = dynamicPageService.getSlideContents(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of SlideContent.", slideContents));
    }

    @GetMapping("/diseases")
    public ResponseEntity<?> getDiseases() {
        List<String> diseasesList = slideContentService.getDiseasesList();
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Diseases.", diseasesList));
    }

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Access forbidden");
    }
}