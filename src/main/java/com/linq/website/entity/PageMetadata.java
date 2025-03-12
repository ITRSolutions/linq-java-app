package com.linq.website.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "page_metadata")
public class PageMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String email;
    private String address;
    private String fbUrl;
    private String xUrl;
    private String instaUrl;
    private String loginUrl;
    private String logoImgUrl;
    private String logoUrl;
    private String buttonText;
    private String buttonUrl;
    private String bottomHeader;
    private String fontFamily;
    @Column(length = 2000)
    private String seoCode;
    private String version;

}
