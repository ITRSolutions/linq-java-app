package com.linq.website.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class SlideDTO {

    // Base SlideDTO fields that are common to both Create and Update
    @Getter
    @Setter
    public static class BaseSlideDTO {

        @NotNull(message = "Slide Title is required")
        @Size(min = 1, message = "Slide Title must be between 1 and 50 characters")
        private String slideTitle;

        @NotNull(message = "orderIndex is required")
        @Min(value = 1, message = "Order Index must be greater than or equal to 1")
        private Integer orderIndex;
    }

    @Getter
    @Setter
    public static class CreateSlideDTO extends BaseSlideDTO {
        @NotNull(message = "Content Block is required")
        private Long contentBlockId;
    }

    @Getter
    @Setter
    public static class UpdateSlideDTO extends BaseSlideDTO {
        @NotNull(message = "Status cannot be null")
        private Boolean slideActive;
    }
}