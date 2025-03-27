package com.linq.website.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linq.website.entity.CompanyPageMetaData;
import com.linq.website.repository.CompanyPageMetaDataRepository;

@Service
public class CompanyPageMetaDataService {

    @Autowired
    private CompanyPageMetaDataRepository companyPageMetaDataRepository;

    public CompanyPageMetaData getCompanyPageMetaData() {
        CompanyPageMetaData metadata  = companyPageMetaDataRepository.findFirstByOrderByIdAsc();
        if(metadata  == null) {
            throw new RuntimeException("Page metadata not found");
        }
        return metadata;
    }

}
