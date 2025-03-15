package com.linq.website.dto;

import com.linq.website.enums.PageStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class DynamicPageDTO {

    @Getter
    @Setter
    public static class CreateDynamicPage {
        @NotEmpty(message = "Title cannot be empty")
        @Size(max = 50, message = "Title cannot be longer than 50 characters")
        private String title;

        @NotEmpty(message = "Slug cannot be empty")
        @Size(max = 50, message = "Slug cannot be longer than 50 characters")
        private String slug;

        @NotNull(message = "Status cannot be null")
        private PageStatus status;
    }

    @Getter
    @Setter
    public static class UpdateDynamicPage {
        @NotEmpty(message = "Title cannot be empty")
        @Size(max = 50, message = "Title cannot be longer than 50 characters")
        private String title;

        @NotEmpty(message = "Slug cannot be empty")
        @Size(max = 50, message = "Slug cannot be longer than 50 characters")
        private String slug;

        @NotNull(message = "Status cannot be null")
        private PageStatus status;
    }

    @Getter
    @Setter
    public static class SearchDynamicPage {
        @NotEmpty(message = "Slug cannot be empty")
        @Size(max = 50, message = "Slug cannot be longer than 50 characters")
        private String slug;
    }
}
