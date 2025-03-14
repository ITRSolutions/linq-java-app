package com.linq.website.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ContentBlockDTO {

    @Getter
    @Setter
    public static class CreateContentBlock {

        @NotNull(message = "Page ID cannot be null")
        private Long pageId;

        @NotEmpty(message = "Content cannot be empty")
        @Size(max = 50, message = "Content cannot be longer than 50 characters")
        private String content;

        @NotNull(message = "Order Index cannot be null")
        private Integer orderIndex;

        @NotNull(message = "Updated By cannot be null")
        private Long updatedById;
    }

    @Getter
    @Setter
    public static class UpdateContentBlock {

        @NotNull(message = "Content Block ID cannot be null")
        private Long id;

        @NotEmpty(message = "Content cannot be empty")
        @Size(max = 50, message = "Content cannot be longer than 50 characters")
        private String content;

        @NotNull(message = "Order Index cannot be null")
        private Integer orderIndex;

        @NotNull(message = "Updated By cannot be null")
        private Long updatedById;
    }
}