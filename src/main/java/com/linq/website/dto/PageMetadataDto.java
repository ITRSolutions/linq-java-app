package com.linq.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageMetadataDto {
    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "Email address is required.")
    @Email(message = "Email address should be valid.")
    private String email;
    @NotBlank(message = "address cannot be empty")
    private String address;
    @NotBlank(message = "fbUrl cannot be empty")
    private String fbUrl;
    @NotBlank(message = "xUrl cannot be empty")
    private String xUrl;
    @NotBlank(message = "instaUrl cannot be empty")
    private String instaUrl;
    @NotBlank(message = "loginUrl cannot be empty")
    private String loginUrl;
    @NotBlank(message = "logoImgUrl cannot be empty")
    private String logoImgUrl;
    @NotBlank(message = "logoUrl cannot be empty")
    private String logoUrl;
    @NotBlank(message = "buttonText cannot be empty")
    private String buttonText;
    @NotBlank(message = "buttonUrl cannot be empty")
    private String buttonUrl;
    @NotBlank(message = "bottomHeader cannot be empty")
    private String bottomHeader;
    private String fontFamily;
    private String seoCode;
    @NotBlank(message = "version cannot be empty")
    private String version;
}
