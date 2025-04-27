package com.linq.website.service;

import com.linq.website.dto.PageMetadataDto;
import com.linq.website.entity.PageMetadata;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.exceptions.ResourceNotFoundException;
import com.linq.website.repository.PageMetadataRepository;
import com.linq.website.utility.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageMetadataService {

    @Autowired
    private PageMetadataRepository pageMetadataRepository;

    @Autowired
    LoggedUser loggedUser;

    @Cacheable(value = "companyMetaData", key = "'companyMetaData'")
    public PageMetadata getPageMetaData() {
        Optional<PageMetadata> metadata  = pageMetadataRepository.findById(1L);
        return metadata.orElse(null);
    }

    public void updatePageMetaData(PageMetadataDto dto) {
        PageMetadata pageMetadata = pageMetadataRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("PageMetadata not found"));

        pageMetadata.setFontFamily(dto.getFontFamily());
        pageMetadata.setBottomHeader(dto.getBottomHeader());
        pageMetadata.setFbUrl(dto.getFbUrl());
        pageMetadata.setInstaUrl(dto.getInstaUrl());
        pageMetadata.setXUrl(dto.getXUrl());
        pageMetadata.setLoginUrl(dto.getLoginUrl());
        pageMetadata.setLogoImgUrl(dto.getLogoImgUrl());
        pageMetadata.setButtonText(dto.getButtonText());
        pageMetadata.setButtonUrl(dto.getButtonUrl());
        pageMetadata.setAddress(dto.getAddress());
        pageMetadata.setPhoneNumber(dto.getPhoneNumber());
        pageMetadata.setEmail(dto.getEmail());
        pageMetadata.setVersion(dto.getVersion());
        pageMetadata.setSeoCode(dto.getSeoCode());

        pageMetadata.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        pageMetadataRepository.save(pageMetadata);
    }
}
