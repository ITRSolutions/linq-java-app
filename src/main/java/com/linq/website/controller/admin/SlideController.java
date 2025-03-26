package com.linq.website.controller.admin;

import com.linq.website.dto.SlideDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.Slide;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.SlideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slide")
@PreAuthorize("hasRole('ADMIN')") //  Ensures only ADMIN can access
public class SlideController {

    @Autowired
    private SlideService slideService;

    @Autowired
    private DynamicPageService dynamicPageService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> createSlide(@Valid @RequestBody SlideDTO.CreateSlideDTO slideDTO) {
        slideService.createSlide(slideDTO);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateSlide(@Valid @RequestBody SlideDTO.UpdateSlideDTO slideDTO, @PathVariable Long id) {
        slideService.updateSlide(slideDTO, id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteSlide(@PathVariable Long id) {
        slideService.deleteSlide(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide is deleted successfully id: "+id, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getSlideByContentBlockId(@PathVariable Long id) {
        List<Slide> slides = dynamicPageService.getSlides(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "List of slide.", slides));
    }

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Access forbidden");
    }
}