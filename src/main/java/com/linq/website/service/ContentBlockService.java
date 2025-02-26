package com.linq.website.service;

import com.linq.website.entity.ContentBlock;
import com.linq.website.repository.ContentBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentBlockService {

    @Autowired
    private ContentBlockRepository contentBlockRepository;

    // Save or update a content block
    public ContentBlock saveContentBlock(ContentBlock contentBlock) {
        return contentBlockRepository.save(contentBlock);
    }

    // Get all content blocks for a given page ID, ordered by their display order
    public List<ContentBlock> getContentBlocksByPageId(Long pageId) {
        return contentBlockRepository.findByPageIdOrderByOrderIndex(pageId);
    }

    // Get a content block by its ID
    public Optional<ContentBlock> getContentBlockById(Long id) {
        return contentBlockRepository.findById(id);
    }

    // Delete a content block by ID
    public void deleteContentBlock(Long id) {
        contentBlockRepository.deleteById(id);
    }

    // Other business logic related to content blocks can be added here
}
