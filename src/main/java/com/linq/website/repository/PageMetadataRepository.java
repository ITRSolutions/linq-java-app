package com.linq.website.repository;

import com.linq.website.entity.PageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageMetadataRepository extends JpaRepository<PageMetadata, Long> {

}
