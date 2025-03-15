package com.linq.website.controller.admin;

import com.linq.website.dto.SlideDTO;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slides")
public class SlideController {

    @Autowired
    private SlideService slideService;

    @PostMapping()
    public ResponseEntity<SuccessResponse> createSlide(@RequestBody SlideDTO.CreateSlideDTO slideDTO) {
        slideService.createSlide(slideDTO);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide created successfully.", null));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateSlide(@RequestBody SlideDTO.CreateSlideDTO slideDTO) {
        slideService.createSlide(slideDTO);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Slide created successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlide(@PathVariable Long id) {
        slideService.deleteSlide(id);
        return ResponseEntity.noContent().build();
    }
}