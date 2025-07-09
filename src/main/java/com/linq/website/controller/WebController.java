package com.linq.website.controller;

import com.linq.website.entity.*;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.PageMetadataService;
import com.linq.website.utility.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private DynamicPageService dynamicPageService;

    @Autowired
    private PageMetadataService pageMetadataService;

    private static final String YEAR = "year";
    private static final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    // Fetch dynamic page by slug and display the page using Thymeleaf
    @GetMapping({"/", "/{slug}"})
    public String getPage(@PathVariable(required = false) String slug, Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          @RequestParam(value = "page", defaultValue = "") String paramPageName) {

        if (slug == null || slug.isEmpty()) {
            slug = "index";
        } else if(slug.equals("admin_panel") || (userDetails != null && slug.equals("login"))) {
            System.out.println("Redirecting to login...");
            return "redirect:/admin_panel/";
        }

        System.out.println("slug: "+slug);

        List<ContentBlock> CB = new ArrayList<>(); //this will be used for blog pages only
        String tempPageName = "";
        if(!paramPageName.isEmpty()) {
            tempPageName = slug;
            slug = paramPageName;
        } else if (paramPageName.isEmpty() && slug.equals("meet-our-principal-investigators")) {
            tempPageName = slug;
            slug = "family-medicine-specialist";
            paramPageName = "meet-our-principal-investigators";
            model.addAttribute("navigTitle", slug);
        }

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
                }

                // Add slides to the content block
                contentBlock.setSlide(slides);
                System.out.println("getContent: "+contentBlock.getContent());

                if(slug.equals("resources") || slug.equals("news-and-events")) {
                    CB.add(contentBlock);
                } else {
                    // Add attributes to the model for Thymeleaf
                    model.addAttribute("contentBlock"+count, contentBlock);
                }
                count++;
            }

            if(slug.equals("resources") || slug.equals("news-and-events")) {
                //Get last element form CB for display Company footer on page.
                model.addAttribute("footerBlockForNewsResources", CB.get(CB.size() - 1));

                CB.remove(CB.size() - 1);
                model.addAttribute("articlePage", CB);
            }

            //Cached
            PageMetadata pageMetaData = pageMetadataService.getPageMetaData();
            model.addAttribute("pageMetaData", pageMetaData);

            model.addAttribute("title", page.getTitle());

            //Navigation links
            //Cached
//            List<ContentBlock> navigationBlocks = getDynamicPageData("navigation");
            List<ContentBlock> navigationBlocks = getNavigationBar();
            model.addAttribute("navigation", navigationBlocks);

            //Bottom links
            //Cached
//            List<ContentBlock> footerBlocks = getDynamicPageData("footer");
            List<ContentBlock> footerBlocks = getFooterBlocks();
            model.addAttribute("footerBlocks", footerBlocks);

            model.addAttribute(YEAR, currentYear); //footer line

            if(slug.equals("meet-our-principal-investigators") || tempPageName.equals("meet-our-principal-investigators")) {
                List<ContentBlock> piNavigation = getDynamicPageData("principal-Investigators-navigation");
                model.addAttribute("pi_Navigation", piNavigation);
                model.addAttribute("navigTitle", slug);
            } else if(slug.equals("faqs") || slug.equals("clinical-trial-process")) {
                List<ContentBlock> faqAllQuestions = getDynamicPageData("faq-all-questions");
                model.addAttribute("faqAllQuestions", faqAllQuestions);
            }
//            else if (slug.equals("why-linq") || slug.equals("investigators")) {
//                List<ContentBlock> therapeutic = getDynamicPageData("therapeutic-block");
//                model.addAttribute("therapeutic", therapeutic);
//            }

            if(!paramPageName.isEmpty()) {
                slug = tempPageName;
            }

            System.out.println("Render slug: "+slug);
            return "public/" +slug; // Thymeleaf page name
        } catch (PageNotFoundException ex) {
            logger.error("PageNotFound : "+ex.getMessage());
            throw new PageNotFoundException("PageNotFound : "+ex.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "footerBlocks", key = "'footerBlocks'")
    public List<ContentBlock> getFooterBlocks() {
        return getDynamicPageData("footer");
    }

    @Cacheable(value = "navigation", key = "'navigation'")
    public List<ContentBlock> getNavigationBar() {
        return getDynamicPageData("navigation");
    }

    public List<ContentBlock> getDynamicPageData(String slug) {
        DynamicPage navigation = dynamicPageService.getPageBySlug(slug);
        List<ContentBlock> navigationBlocks = dynamicPageService.getContentBlocks(navigation.getId());

        for (ContentBlock contentBlock : navigationBlocks) {
            List<Slide> slides = dynamicPageService.getSlides(contentBlock.getId());

            for (Slide slide : slides) {
                List<SlideContent> slideContents = dynamicPageService.getSlideContents(slide.getId());
                slide.setSlideContents(slideContents); // Setting slide contents to the slide object

                // Debugging: print slide info
                System.out.println(slug+" slide ID: " + slide.getId() + " has " + slideContents.size() + " navigation slideContents.");
            }

            // Add slides to the content block
            contentBlock.setSlide(slides);
        }

        return navigationBlocks;
    }

//    @GetMapping("/employee_registration")
//    public String employeeRegistration(Model model) {
//        return "registration_form/employee_registration";
//    }

    @GetMapping("/appointment_form")
    public String appointmentForm(@RequestParam(value = "disease") String disease, Model model) {
        if(!disease.isEmpty()) {
            disease = disease.replace(" ","-").replaceAll("[^a-zA-Z0-9-]", "").toLowerCase();
            model.addAttribute("pageName", disease);

            PageMetadata pageMetaData = pageMetadataService.getPageMetaData();
            model.addAttribute("LogoImgUrl", pageMetaData.getLogoImgUrl());

            model.addAttribute(YEAR, currentYear);
            return "public/appointment_form";
        }
        return "redirect:/public/current-enrollment";
    }

    @GetMapping("/refer_friend_form")
    public String referFriendForm(Model model) {
        model.addAttribute(YEAR, currentYear);
        return "public/refer_friend_form";
    }

    @GetMapping("/prinicipal_investigator_form")
    public String prinicipalInvestigatorForm(Model model) {
        model.addAttribute(YEAR, currentYear);
        return "public/prinicipal_investigator_form";
    }

    @GetMapping("/activationError")
    public String activationError(@RequestParam("message") String message, Model model) {
        model.addAttribute("errorMessage", message);
        return "mail/activationError";
    }
}
