package com.linq.website.service;

import com.linq.website.controller.WebController;
import com.linq.website.dto.DynamicPageDTO;
import com.linq.website.entity.*;
import com.linq.website.enums.PageStatus;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.repository.*;
import com.linq.website.utility.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

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

    @Autowired
    LoggedUser loggedUser;

    @Autowired
    SlugCacheEvict slugCacheEvict;

    // Fetch dynamic page by slug
    public DynamicPage getPageBySlug(String slug) {
        return dynamicPageRepository.findBySlugIgnoreCaseAndStatus(slug, PageStatus.PUBLISHED)
                .orElseThrow(() -> new PageNotFoundException("Page not found with name: " + slug));
    }

    public DynamicPage getPageByName(String slug) {
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
        return slideRepository.findByContentBlockAndSlideActiveTrue(contentBlock);
    }

    // Fetch slides for a content block
    public List<Slide> getSlidesForAdminPanel(Long contentBlockId) {
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

    public void saveDynamicPage(DynamicPageDTO.CreateDynamicPage dto) {

        DynamicPage dynamicPage = new DynamicPage();
        dynamicPage.setTitle(dto.getTitle());
        dynamicPage.setSlug(dto.getSlug());
        dynamicPage.setStatus(dto.getStatus());
        dynamicPage.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        dynamicPageRepository.save(dynamicPage);
    }

    public void updateDynamicPage(DynamicPageDTO.UpdateDynamicPage dto, Long id) {
        // Fetch the DynamicPage by ID
        DynamicPage dynamicPage = dynamicPageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DynamicPage not found"));

        // Store old slug for cache eviction if slug changed
        String oldSlug = dynamicPage.getSlug();

        dynamicPage.setTitle(dto.getTitle());
        dynamicPage.setSlug(dto.getSlug());
        dynamicPage.setStatus(dto.getStatus());
        dynamicPage.setUpdatedBy(loggedUser.getUpdatedByUserObj());  // Set the logged-in user (admin)

        // Save the updated DynamicPage object
        dynamicPageRepository.save(dynamicPage);

        // Evict cache if slug changed
        if (!oldSlug.equals(dynamicPage.getSlug())) {
            slugCacheEvict.evictPageCache(oldSlug); // Evict old slug cache
        }
        slugCacheEvict.evictPageCache(dynamicPage.getSlug()); // Evict current slug
    }

    public Page<DynamicPage> getDynamicPagesWithPagination(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "id"));

        // Fetch paginated results from the repository
        return dynamicPageRepository.findAll(pageable);
    }

    public List<DynamicPage> searchDynamicPages(DynamicPageDTO.SearchDynamicPage searchDTO) {
        return dynamicPageRepository.search(searchDTO.getSlug());
    }

    public Long getCountPageStatus(PageStatus status) {
        return dynamicPageRepository.countByStatus(status);
    }

    public List<ContentBlock> getFooterBlocks() {
        return getDynamicPageDataInternal("footer");
    }

    public List<ContentBlock> getNavigationBar() {
        return getDynamicPageDataInternal("navigation");
    }

    public List<ContentBlock> getPrincipalInvestigatorsNavigation() {
        return getDynamicPageDataInternal("principal-Investigators-navigation");
    }

    public List<ContentBlock> getMeetOurPrincipalInvestigators() {
        return getDynamicPageDataInternal("meet-our-principal-investigators");
    }

    public List<ContentBlock> getFaqAllQuestions() {
        return getDynamicPageDataInternal("faq-all-questions");
    }

    public List<ContentBlock> getDynamicPageDataInternal(String slug) {
        return (List<ContentBlock>) getDynamicPageDataInternal(slug, false);
    }

    @Cacheable(value = "dynamicPageData", key = "#slug")
    public Object getDynamicPageDataInternal(String slug, Boolean returnModel) {
        DynamicPage navigation = getPageBySlug(slug);
        List<ContentBlock> navigationBlocks = getContentBlocks(navigation.getId());
        Model model = new ExtendedModelMap();
        int count = 1;

        for (ContentBlock contentBlock : navigationBlocks) {
            List<Slide> slides = getSlides(contentBlock.getId());

            for (Slide slide : slides) {
                List<SlideContent> slideContents = getSlideContents(slide.getId());
                slide.setSlideContents(slideContents); // Setting slide contents to the slide object

                // Debugging: print slide info
//                System.out.println(slug+" slide ID: " + slide.getId() + " has " + slideContents.size() + " navigation slideContents.");
            }

            // Add slides to the content block
            contentBlock.setSlide(slides);
            if(returnModel) {
                model.addAttribute("contentBlock"+count, contentBlock);
                count++;
            }
        }

        if (returnModel) {
            model.addAttribute("title", navigation.getTitle());
            return model;
        }

        return navigationBlocks;
    }

}
