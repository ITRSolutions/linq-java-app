package com.linq.website.repository;

import com.linq.website.entity.ContentBlock;
import com.linq.website.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {
    List<Slide> findByContentBlock(ContentBlock contentBlock);

    List<Slide> findByContentBlockAndSlideActiveTrue(ContentBlock contentBlock);

}
