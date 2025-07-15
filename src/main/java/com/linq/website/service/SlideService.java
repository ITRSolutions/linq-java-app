package com.linq.website.service;

import com.linq.website.dto.SlideDTO;
import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.Slide;
import com.linq.website.repository.ContentBlockRepository;
import com.linq.website.repository.SlideRepository;
import com.linq.website.repository.UserRepository;
import com.linq.website.utility.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    LoggedUser loggedUser;

    @Autowired
    SlugCacheEvict slugCacheEvict;

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
        slide.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        slideRepository.save(slide);
    }

    public void deleteSlide(Long id) {
        slideRepository.deleteById(id);
    }

    public void updateSlide(SlideDTO.UpdateSlideDTO dto, Long id) {
        // Validate and fetch slide block by ID
        Optional<Slide> existingSlideOpt = slideRepository.findById(id);
        if (existingSlideOpt.isEmpty()) {
            throw new RuntimeException("ContentBlock not found");
        }
        Slide slideBlock = existingSlideOpt.get();

        // Update fields
        slideBlock.setSlideTitle(dto.getSlideTitle());
        slideBlock.setSlideActive(dto.getSlideActive());
        slideBlock.setOrderIndex(dto.getOrderIndex());
        slideBlock.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        // Save the updated Slide
        slideRepository.save(slideBlock);

        //Evict cache
        String slug = slideBlock.getContentBlock().getPage().getSlug();
        if(Optional.ofNullable(slug).isPresent()) {
            slugCacheEvict.evictPageCache(slug);
        }
    }

}