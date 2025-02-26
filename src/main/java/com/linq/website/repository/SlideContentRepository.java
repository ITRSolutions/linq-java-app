package com.linq.website.repository;

import com.linq.website.entity.Slide;
import com.linq.website.entity.SlideContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SlideContentRepository extends JpaRepository<SlideContent, Long> {
    List<SlideContent> findBySlide(Slide slide);
}
