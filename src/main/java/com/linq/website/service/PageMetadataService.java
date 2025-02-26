package com.linq.website.service;

import com.linq.website.entity.PageMetadata;
import com.linq.website.repository.PageMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageMetadataService {

    @Autowired
    private PageMetadataRepository pageMetadataRepository;

    public PageMetadata getPageMetaData() {
        PageMetadata metadata  = pageMetadataRepository.findFirstByOrderByIdAsc();
        if(metadata  == null) {
            throw new RuntimeException("Page metadata not found");
        }
        return metadata;
    }
}
