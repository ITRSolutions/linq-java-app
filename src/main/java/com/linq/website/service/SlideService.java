package com.linq.website.service;

import com.linq.website.dto.SlideDTO;
import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.Slide;
import com.linq.website.entity.User;
import com.linq.website.repository.ContentBlockRepository;
import com.linq.website.repository.SlideRepository;
import com.linq.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SlideService {

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ContentBlockRepository contentBlockRepository;

    public void createSlide(SlideDTO.CreateSlideDTO dto) {
        // Validate and fetch page and user details
        Optional<ContentBlock> contentBlockOpt = contentBlockRepository.findById(dto.getContentBlockId());
        if (contentBlockOpt.isEmpty()) {
            throw new RuntimeException("ContentBlock not found");
        }
        ContentBlock contentBlock = contentBlockOpt.get();

        Slide slide = new Slide();
        slide.setSlideTitle(dto.getSlideTitle());
        slide.setContentBlock(contentBlock);
        slide.setOrderIndex(dto.getOrderIndex());
        slide.setUpdatedBy(getUpdatedByUserObj());

        slideRepository.save(slide);
    }

    public User getUpdatedByUserObj() {
        // Fetch the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        // Get the User object using the username
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found. Please re-login"));
    }

    public void deleteSlide(Long id) {
        slideRepository.deleteById(id);
    }

    public void updateSlide(SlideDTO.UpdateSlideDTO dto) {
        // Validate and fetch slide block by ID
        Optional<Slide> existingSlideOpt = slideRepository.findById(dto.getId());
        if (existingSlideOpt.isEmpty()) {
            throw new RuntimeException("ContentBlock not found");
        }
        Slide slideBlock = existingSlideOpt.get();

        // Update fields
        slideBlock.setSlideTitle(dto.getSlideTitle());
        slideBlock.setOrderIndex(dto.getOrderIndex());
        slideBlock.setUpdatedBy(getUpdatedByUserObj());

        // Save the updated Slide
        slideRepository.save(slideBlock);
    }
}