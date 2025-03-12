package com.linq.website.controller;

import com.linq.website.entity.*;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.MailService;
import com.linq.website.service.PageMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private DynamicPageService dynamicPageService;

    @Autowired
    private PageMetadataService pageMetadataService;

    // Fetch dynamic page by slug and display the page using Thymeleaf
    @GetMapping("/{slug}")
    public String getPage(@PathVariable String slug, Model model) {
//        String slug= "index";
        System.out.println("slug: "+slug);
        try {
            // Fetch dynamic page by slug
            DynamicPage page = dynamicPageService.getPageBySlug(slug);
            // Fetch content blocks related to the page
            List<ContentBlock> contentBlocks = dynamicPageService.getContentBlocks(page.getId());

            int count = 1;
            // List to hold all slides and slide content
            for (ContentBlock contentBlock : contentBlocks) {
                // Fetch slides related to the current content block
                List<Slide> slides = dynamicPageService.getSlides(contentBlock.getId());

                // Debugging: print slide info
                System.out.println("ContentBlock ID: " + contentBlock.getId() + " has " + slides.size() + " slides.");

                // Fetch slide content for each slide
                for (Slide slide : slides) {
                    List<SlideContent> slideContents = dynamicPageService.getSlideContents(slide.getId());
                    slide.setSlideContents(slideContents); // Setting slide contents to the slide object

                    // Debugging: print slide info
                    System.out.println("slide ID: " + slide.getId() + " has " + slideContents.size() + " slideContents.");
                    System.out.println("slideContents: "+slideContents);
                }

                // Add slides to the content block
                contentBlock.setSlide(slides);

                System.out.println("getContent: "+contentBlock.getContent());
                // Add attributes to the model for Thymeleaf
                model.addAttribute("contentBlock"+count, contentBlock);
                count++;
            }

            PageMetadata pageMetaData = pageMetadataService.getPageMetaData();
            model.addAttribute("pageMetaData", pageMetaData);
            model.addAttribute("title", page.getTitle());

            //Navigation links
            List<ContentBlock> navigationBlocks = getNavigationSlides("navigation");
            model.addAttribute("navigation", navigationBlocks);

            //Bottom links
            List<ContentBlock> footerBlocks = getNavigationSlides("footer_link");
            model.addAttribute("footerBlocks", footerBlocks);

            return slug; // Thymeleaf page name
        } catch (PageNotFoundException ex) {
            // Handle exception and redirect to error page
            model.addAttribute("error", ex.getMessage());
            return "error/404"; // This will redirect to the 404 page
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error/404"; // This will redirect to the 404 page
        }
    }

    public List<ContentBlock> getNavigationSlides(String slug) {
        DynamicPage navigation = dynamicPageService.getPageBySlug(slug);
        List<ContentBlock> navigationBlocks = dynamicPageService.getContentBlocks(navigation.getId());

        for (ContentBlock contentBlock : navigationBlocks) {
            List<Slide> slides = dynamicPageService.getSlides(contentBlock.getId());

            for (Slide slide : slides) {
                List<SlideContent> slideContents = dynamicPageService.getSlideContents(slide.getId());
                slide.setSlideContents(slideContents); // Setting slide contents to the slide object

                // Debugging: print slide info
                System.out.println(slug+" slide ID: " + slide.getId() + " has " + slideContents.size() + " navigation slideContents.");
//                System.out.println("navigation slideContents: "+slideContents);
            }

            // Add slides to the content block
            contentBlock.setSlide(slides);
        }

        return navigationBlocks;
    }

    @GetMapping("/employee_registration")
    public String employeeRegistration(Model model) {
        return "employee_registration";
    }

}
