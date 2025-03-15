package com.linq.website.dto;

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
    public static class BaseSlideDTO {

        @NotNull(message = "Content Block is required")
        private Long contentBlockId;

        @NotNull(message = "Slide Title is required")
        @Size(min = 1, max = 50, message = "Slide Title must be between 1 and 50 characters")
        private String slideTitle;

        @Min(value = 1, message = "Order Index must be greater than or equal to 1")
        private Integer orderIndex;

        @NotNull(message = "Updated By is required")
        private Long updatedById;

        private List<Long> slideContentIds;  // Assuming SlideContent references are passed as IDs
    }

    @Getter
    @Setter
    public static class CreateSlideDTO extends BaseSlideDTO {

        // Fields for creating a Slide object
        @Null(message = "ID should be null when creating a new slide")
        private Long id; // ID should not be set during creation

    }

    @Getter
    @Setter
    public static class UpdateSlideDTO extends BaseSlideDTO {

        // Fields for updating a Slide object
        @NotNull(message = "ID is required for update")
        private Long id; // ID must be provided during update
    }
}