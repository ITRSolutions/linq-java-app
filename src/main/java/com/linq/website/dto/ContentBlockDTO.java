package com.linq.website.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ContentBlockDTO {

    @Getter
    @Setter
    public static class BaseContentBlock {

        @NotEmpty(message = "Content cannot be empty")
        @Size(message = "Content cannot be longer than 50 characters")
        private String content;
    }

    @Getter
    @Setter
    public static class CreateContentBlock extends BaseContentBlock {
        @NotNull(message = "Page ID cannot be null")
        private Long pageId;

        @NotNull(message = "Order Index cannot be null")
        private Integer orderIndex;
    }

    @Getter
    @Setter
    public static class UpdateContentBlock extends BaseContentBlock {
        private Long id;

        @NotNull(message = "Order Index cannot be null")
        private Integer orderIndex;
    }
}