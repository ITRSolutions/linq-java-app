package com.linq.website.repository;
import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.DynamicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentBlockRepository extends JpaRepository<ContentBlock, Long> {
    List<ContentBlock> findByPage(DynamicPage page);

    List<ContentBlock> findByPageIdOrderByOrderIndex(Long pageId);
}