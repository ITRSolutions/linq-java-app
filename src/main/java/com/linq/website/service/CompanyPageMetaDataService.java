package com.linq.website.service;


import com.linq.website.exceptions.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.linq.website.entity.CompanyPageMetaData;
import com.linq.website.repository.CompanyPageMetaDataRepository;

import java.util.Optional;

@Service
public class CompanyPageMetaDataService {

    @Autowired
    private CompanyPageMetaDataRepository companyPageMetaDataRepository;

    @Cacheable(value = "pageMetaData", key = "'pageMetaData'")
    public CompanyPageMetaData getCompanyPageMetaData() {
        CompanyPageMetaData metadata  = companyPageMetaDataRepository.findFirstByOrderByIdAsc();
        if(metadata  == null) {
            throw new RuntimeException("Page metadata not found");
        }
        return metadata;
    }

    public void updateData(CompanyPageMetaData obj) {
        CompanyPageMetaData companyPageMetaData = companyPageMetaDataRepository.findById(1l).orElseThrow(() -> new PageNotFoundException("Company Page Meta Data not found"));
        companyPageMetaData.setCompanyName(obj.getCompanyName());
        companyPageMetaData.setParticipateNowText(obj.getParticipateNowText());
        companyPageMetaData.setJoinCompanyDesc(obj.getJoinCompanyDesc());
        companyPageMetaData.setButtonURL(obj.getButtonURL());
        companyPageMetaDataRepository.save(companyPageMetaData);
    }

    public CompanyPageMetaData getData(){
        return companyPageMetaDataRepository.findById(1L).orElseThrow(() -> new PageNotFoundException("Company Page Meta Data not found"));
    }
}
