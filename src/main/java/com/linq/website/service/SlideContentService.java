package com.linq.website.service;

import com.linq.website.dto.SlideContentDTO;
import com.linq.website.entity.Slide;
import com.linq.website.entity.SlideContent;
import com.linq.website.enums.ContentType;
import com.linq.website.repository.SlideContentRepository;
import com.linq.website.repository.SlideRepository;
import com.linq.website.utility.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SlideContentService {

    @Autowired
    private SlideContentRepository slideContentRepository;

    @Autowired
    SlideRepository slideRepository;

    @Autowired
    LoggedUser loggedUser;

    public void createSlideContent(SlideContentDTO.Create dto) {
        Optional<Slide> slideOpt = slideRepository.findById(dto.getSlideId());
        if (slideOpt.isEmpty()) {
            throw new RuntimeException("Slide not found");
        }
        Slide slide = slideOpt.get();

        SlideContent slideContent = new SlideContent();
        slideContent.setSlide(slide);
        slideContent.setContentType(dto.getContentType());
        slideContent.setContent(dto.getContent());
        slideContent.setCustomCss(dto.getCustomCss());
        slideContent.setImageAltText(dto.getImageAltText());
        slideContent.setOrderIndex(dto.getOrderIndex());
        slideContent.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        slideContentRepository.save(slideContent);
    }

    public void deleteSlideContent(Long id) {
        slideContentRepository.deleteById(id);
    }

    public void updateSlide(SlideContentDTO.Update dto, Long id) {
        // Validate and fetch slide block by ID
        Optional<SlideContent> existingSlideContentOpt = slideContentRepository.findById(id);
        if (existingSlideContentOpt.isEmpty()) {
            throw new RuntimeException("ContentBlock not found");
        }
        SlideContent slideContentBlock = existingSlideContentOpt.get();

        // Update fields
        slideContentBlock.setContentType(dto.getContentType());
        slideContentBlock.setContent(dto.getContent());
        slideContentBlock.setImageAltText(dto.getImageAltText());
        slideContentBlock.setOrderIndex(dto.getOrderIndex());
        slideContentBlock.setCustomCss(dto.getCustomCss());
        slideContentBlock.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        // Save the updated SlideContent
        slideContentRepository.save(slideContentBlock);
    }

    public List<String> getDiseasesList() {
        return slideContentRepository.findDistinctContentByContentType(ContentType.DISEASE_NAME);
    }
}