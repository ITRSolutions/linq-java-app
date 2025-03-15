package com.linq.website.service;

import com.linq.website.dto.DynamicPageDTO;
import com.linq.website.entity.*;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private UserRepository userRepository;

    // Fetch dynamic page by slug
    public DynamicPage getPageBySlug(String slug) {
        return dynamicPageRepository.findBySlug(slug)
                .orElseThrow(() -> new PageNotFoundException("Page not found with name: " + slug));
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

    public User getUpdatedByUserObj() {
        // Fetch the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        // Get the User object using the username
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found. Please re-login"));
    }

    public void saveDynamicPage(DynamicPageDTO.CreateDynamicPage dto) {

        DynamicPage dynamicPage = new DynamicPage();
        dynamicPage.setTitle(dto.getTitle());
        dynamicPage.setSlug(dto.getSlug());
        dynamicPage.setStatus(dto.getStatus());
        dynamicPage.setUpdatedBy(getUpdatedByUserObj());

        dynamicPageRepository.save(dynamicPage);
    }

    public void updateDynamicPage(DynamicPageDTO.UpdateDynamicPage dto, Long id) {
        // Fetch the DynamicPage by ID
        DynamicPage dynamicPage = dynamicPageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DynamicPage not found"));
        dynamicPage.setTitle(dto.getTitle());
        dynamicPage.setSlug(dto.getSlug());
        dynamicPage.setStatus(dto.getStatus());
        dynamicPage.setUpdatedBy(getUpdatedByUserObj());  // Set the logged-in user (admin)

        // Save the updated DynamicPage object
        dynamicPageRepository.save(dynamicPage);
    }

    // Find all resources by role with paginated
    public Page<DynamicPage> getDynamicPagesWithPagination(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "id"));

        // Fetch paginated results from the repository
        return dynamicPageRepository.findAll(pageable);
    }

    public List<DynamicPage> searchDynamicPages(DynamicPageDTO.SearchDynamicPage searchDTO) {
        return dynamicPageRepository.search(searchDTO.getSlug());
    }
}
