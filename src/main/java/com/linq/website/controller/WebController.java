package com.linq.website.controller;

import com.linq.website.entity.*;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.PageMetadataService;
import com.linq.website.utility.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.linq.website.service.CompanyPageMetaDataService;

import java.util.Calendar;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private DynamicPageService dynamicPageService;

    @Autowired
    private PageMetadataService pageMetadataService;

    @Autowired
    private CompanyPageMetaDataService companyPageMetaDataService;

    private static final String YEAR = "year";
    private static final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    // Fetch dynamic page by slug and display the page using Thymeleaf
    @GetMapping({"/", "/{slug}"})
    public String getPage(@PathVariable(required = false) String slug, Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (slug == null || slug.isEmpty()) {
            slug = "index";
        } else if(slug.equals("admin_panel") || (userDetails != null && slug.equals("login"))) {
            return "redirect:/admin_panel/";
        }

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
            List<ContentBlock> footerBlocks = getNavigationSlides("footer");
            model.addAttribute("footerBlocks", footerBlocks);

            CompanyPageMetaData cPageMetaData = companyPageMetaDataService.getCompanyPageMetaData();
            model.addAttribute("cPageMetaData", cPageMetaData);

            //Bottom links
            List<ContentBlock> faqAllQuestions = getNavigationSlides("faq-all-questions");
            model.addAttribute("faqAllQuestions", faqAllQuestions);


            List<ContentBlock>  referFrdCommon = getNavigationSlides("refer-frd-area");
            model.addAttribute("referFrdCommonArea", referFrdCommon);



            return "public/" +slug; // Thymeleaf page name
        } catch (RuntimeException ex) {
            System.out.println(ex);
            return "/error/404";
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
        return "registration_form/employee_registration";
    }

    @GetMapping("/activationError")
    public String activationError(@RequestParam("message") String message, Model model) {
        model.addAttribute("errorMessage", message);
        return "mail/activationError";
    }

    @GetMapping("/error_404")
    public String handleError404(Model model) {
        System.out.println("Error page access: "+currentYear);
        model.addAttribute(YEAR, currentYear);
        return "error/error_404"; // This will redirect to the 404 page
    }
}
