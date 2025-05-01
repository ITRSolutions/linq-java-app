package com.linq.website.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ApplicationRequestDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    private String phone;

    @NotNull(message = "eligibility is required.")
    private Boolean eligibility;

    @NotNull(message = "visa is required.")
    private Boolean visa;

    @NotNull(message = "reside is required.")
    private Boolean reside;

    @Size(max = 100, message = "Compensation field must be less than 100 characters.")
    private String compensation;

    @NotNull(message = "pageName file is required.")
    private String pageName;

    @NotNull(message = "Resume file is required.")
    private FileUpload resume;

    private FileUpload coverLetter;

    private String resumeURL;
    private String coverURL;

    @NotNull(message = "jobTitle is required.")
    private String jobTitle;

    @Data
    public static class FileUpload {

        @NotBlank(message = "File name is required.")
        private String fileName;

        @NotBlank(message = "Content type is required.")
        private String contentType;

        @NotBlank(message = "File content (Base64) is required.")
        private String content;
    }
}
