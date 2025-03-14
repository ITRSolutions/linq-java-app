package com.linq.website.service;

import com.linq.website.dto.ContentBlockDTO;
import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.User;
import com.linq.website.repository.ContentBlockRepository;
import com.linq.website.repository.DynamicPageRepository;
import com.linq.website.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentBlockService {

    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DynamicPageRepository dynamicPageRepository;

    // Get all content blocks for a given page ID, ordered by their display order
    public List<ContentBlock> getContentBlocksByPageId(Long pageId) {
        return contentBlockRepository.findByPageIdOrderByOrderIndex(pageId);
    }

    // Get a content block by ID, throwing an exception if not found
    public ContentBlock getContentBlockById(Long id) {
        return contentBlockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content Block not found with id: " + id));
    }

    // Delete a content block by ID
    public void deleteContentBlock(Long id) {
        contentBlockRepository.deleteById(id);
    }

    public void createContentBlock(ContentBlockDTO.CreateContentBlock dto) {
        // Validate and fetch page and user details
        Optional<DynamicPage> pageOpt = dynamicPageRepository.findById(dto.getPageId());
        if (!pageOpt.isPresent()) {
            throw new RuntimeException("Page not found");
        }
        DynamicPage page = pageOpt.get();

        // Fetch the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Get the User object using the username
        User updatedBy = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found. Please re-login"));

        // Create new ContentBlock and set values
        ContentBlock contentBlock = new ContentBlock();
        contentBlock.setPage(page);
        contentBlock.setContent(dto.getContent());
        contentBlock.setOrderIndex(dto.getOrderIndex());
        contentBlock.setUpdatedBy(updatedBy);

        // Save ContentBlock entity
        contentBlockRepository.save(contentBlock);
    }

    public ContentBlock updateContentBlock(ContentBlockDTO.UpdateContentBlock dto) {
        // Validate and fetch content block by ID
        Optional<ContentBlock> existingContentBlockOpt = contentBlockRepository.findById(dto.getId());
        if (!existingContentBlockOpt.isPresent()) {
            throw new RuntimeException("Content Block not found");
        }
        ContentBlock existingContentBlock = existingContentBlockOpt.get();

        Optional<User> updatedByOpt = userRepository.findById(dto.getUpdatedById());
        if (!updatedByOpt.isPresent()) {
            throw new RuntimeException("User not found for updatedBy");
        }
        User updatedBy = updatedByOpt.get();

        // Update fields
        existingContentBlock.setContent(dto.getContent());
        existingContentBlock.setOrderIndex(dto.getOrderIndex());
        existingContentBlock.setUpdatedBy(updatedBy);

        // Save the updated ContentBlock
        return contentBlockRepository.save(existingContentBlock);
    }
}
