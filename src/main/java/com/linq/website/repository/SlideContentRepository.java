package com.linq.website.repository;

import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.Slide;
import com.linq.website.entity.SlideContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SlideContentRepository extends JpaRepository<SlideContent, Long> {
    List<SlideContent> findBySlide(Slide slide);
}
