package com.linq.website.dto;

import com.linq.website.enums.ContentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SlideContentDTO {

    // Base SlideDTO fields that are common to both Create and Update

    @Getter
    @Setter
    private static class Base {
        @NotNull(message = "Content type cannot be null")
        private ContentType contentType;

        @NotNull(message = "Content cannot be null")
        @Size(max = 5000, message = "Content length cannot exceed 5000 characters")
        private String content;

        private String imageAltText;

        @NotNull(message = "Order index cannot be null")
        private Integer orderIndex;

        // Optional fields for create
        private String customCss;
    }

    @Getter
    @Setter
    public static class Create extends Base {
        @NotNull(message = "Slide cannot be null")
        private Long slideId;
    }

    @Getter
    @Setter
    public static class Update extends Base {

    }
}