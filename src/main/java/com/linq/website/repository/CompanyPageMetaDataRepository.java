package com.linq.website.repository;

import com.linq.website.entity.CompanyPageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyPageMetaDataRepository extends JpaRepository<CompanyPageMetaData, Long> {

    CompanyPageMetaData findFirstByOrderByIdAsc();

}

