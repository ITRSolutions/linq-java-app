package com.linq.website.controller;

import com.linq.website.entity.*;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.service.PageMetadataService;
import com.linq.website.utility.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private DynamicPageService dynamicPageService;

    @Autowired
    private PageMetadataService pageMetadataService;

    @Autowired
    private CacheManager cacheManager;

    private static final String YEAR = "year";
    private static final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    // Fetch dynamic page by slug and display the page using Thymeleaf
    @GetMapping({"/", "/{slug}"})
    public String getPage(@PathVariable(required = false) String slug, Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          @RequestParam(value = "page", defaultValue = "") String paramPageName) {

        if (Optional.ofNullable(slug).isEmpty()) {
            slug = "index";
            System.out.println("slug init Done: ");
        } else if(slug.equals("admin_panel") || (userDetails != null && slug.equals("login"))) {
            System.out.println("Redirecting to login...");
            return "redirect:/admin_panel/";
        }

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

        System.out.println("slug: "+slug);

        //Currently we are avoiding this 2 pages to get cached. In future need to update
        boolean isAvoidCachePage = (slug.equals("resources") || slug.equals("news-and-events"));

        try {
            if(paramPageName.isEmpty() && !isAvoidCachePage) {
                //fetch data from cache
                Model populatedModel = (Model) dynamicPageService.getDynamicPageDataInternal(slug, true);

                // Copy attributes from populatedModel into the real controller model
                model.addAllAttributes(((ExtendedModelMap) populatedModel).asMap());

                System.out.println("Cache page render: "+slug);
            } else {
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
//                    System.out.println("ContentBlock ID: " + contentBlock.getId() + " has " + slides.size() + " slides.");

                    // Fetch slide content for each slide
                    for (Slide slide : slides) {
                        List<SlideContent> slideContents = dynamicPageService.getSlideContents(slide.getId());
                        slide.setSlideContents(slideContents); // Setting slide contents to the slide object

                        // Debugging: print slide info
//                        System.out.println("slide ID: " + slide.getId() + " has " + slideContents.size() + " slideContents.");
                    }

                    // Add slides to the content block
                    contentBlock.setSlide(slides);
                    System.out.println("getContent: "+contentBlock.getContent());

                    if(isAvoidCachePage) {
                        CB.add(contentBlock);
                    } else {
                        // Add attributes to the model for Thymeleaf
                        model.addAttribute("contentBlock"+count, contentBlock);
                    }
                    count++;
                }
                model.addAttribute("title", page.getTitle());
            }

            if(isAvoidCachePage) {
                //Get last element form CB for display Company footer on page.
                model.addAttribute("footerBlockForNewsResources", CB.get(CB.size() - 1));

                CB.remove(CB.size() - 1);
                model.addAttribute("articlePage", CB);
            }

            //Cached
            PageMetadata pageMetaData = pageMetadataService.getPageMetaData();
            model.addAttribute("pageMetaData", pageMetaData);

            //Navigation links
            //Cached
//            List<ContentBlock> navigationBlocks = getDynamicPageData("navigation");
            List<ContentBlock> navigationBlocks = dynamicPageService.getNavigationBar();
            model.addAttribute("navigation", navigationBlocks);

            //Bottom links
            //Cached
//            List<ContentBlock> footerBlocks = getDynamicPageData("footer");
            List<ContentBlock> footerBlocks = dynamicPageService.getFooterBlocks();
            model.addAttribute("footerBlocks", footerBlocks);

            model.addAttribute(YEAR, currentYear); //footer line

            if(slug.equals("meet-our-principal-investigators") || tempPageName.equals("meet-our-principal-investigators")) {
                List<ContentBlock> piNavigation = dynamicPageService.getPrincipalInvestigatorsNavigation();
                model.addAttribute("pi_Navigation", piNavigation);
                model.addAttribute("navigTitle", slug);

                //Company footer block code
                List<ContentBlock> principalInvestCompanyFooter = dynamicPageService.getMeetOurPrincipalInvestigators();
                model.addAttribute("principalInvestCompanyFooter", principalInvestCompanyFooter.get(0));
            } else if(slug.equals("faqs") || slug.equals("clinical-trial-process")) {
                List<ContentBlock> faqAllQuestions = dynamicPageService.getFaqAllQuestions();
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

    @GetMapping("/cache/{slug}")
    @ResponseBody
    public String checkCache(@PathVariable String slug) {
        Cache cache = cacheManager.getCache("dynamicPageData");
        if (cache == null) {
            return "ERROR: Cache region 'dynamicPageData' not found\n" +
                    "Available caches: " + String.join(", ", cacheManager.getCacheNames());
        }

        // Safe type conversion for Caffeine cache
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache =
                (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache.getNativeCache();

        Cache.ValueWrapper wrapper = cache.get(slug);
        if (wrapper == null) {
            return "EVICTED: No cache entry for '" + slug + "'\n" +
                    "Existing keys: " + caffeineCache.asMap().keySet();
        }

        Object cachedValue = wrapper.get();
        return String.format("""
        EXISTS: %s
        Type: %s
        Content sample: %s
        """,
                slug,
                cachedValue.getClass().getName(),
                cachedValue.toString().substring(0, Math.min(100, cachedValue.toString().length())));
    }
}
