package com.linq.website.service;

import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.Slide;
import com.linq.website.entity.SlideContent;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.repository.ContentBlockRepository;
import com.linq.website.repository.DynamicPageRepository;
import com.linq.website.repository.SlideContentRepository;
import com.linq.website.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicPageService {

    @Autowired
    private DynamicPageRepository dynamicPageRepository;

    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private SlideContentRepository slideContentRepository;

    // Fetch dynamic page by slug
    public DynamicPage getPageBySlug(String slug) {
        DynamicPage page = dynamicPageRepository.findBySlug(slug);
        if(page == null) {
            throw new PageNotFoundException("Page not found with name: " + slug);
        }
        return page;
    }

    // Fetch content blocks for a page
    public List<ContentBlock> getContentBlocks(Long pageId) {
        DynamicPage page = dynamicPageRepository.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("Page not found with ID: " + pageId));
        return contentBlockRepository.findByPage(page);
    }

    // Fetch slides for a content block
    public List<Slide> getSlides(Long contentBlockId) {
        ContentBlock contentBlock = contentBlockRepository.findById(contentBlockId)
                .orElseThrow(() -> new PageNotFoundException("Content Block not found with ID: " + contentBlockId));
        return slideRepository.findByContentBlock(contentBlock);
    }

    // Fetch slide content (text or image)
    public List<SlideContent> getSlideContents(Long slideId) {
        Slide slide = slideRepository.findById(slideId)
                .orElseThrow(() -> new PageNotFoundException("Slide not found with ID: " + slideId));
        return slideContentRepository.findBySlide(slide);
    }
}
