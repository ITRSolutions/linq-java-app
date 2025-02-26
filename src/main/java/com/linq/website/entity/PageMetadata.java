package com.linq.website.entity;

import jakarta.persistence.*;

@Entity
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
    @Column(length = 2000)
    private String seoCode;
    private String version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public String getXUrl() {
        return xUrl;
    }

    public void setXUrl(String xUrl) {
        this.xUrl = xUrl;
    }

    public String getInstaUrl() {
        return instaUrl;
    }

    public void setInstaUrl(String instaUrl) {
        this.instaUrl = instaUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getBottomHeader() {
        return bottomHeader;
    }

    public void setBottomHeader(String bottomHeader) {
        this.bottomHeader = bottomHeader;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeoCode() {
        return seoCode;
    }

    public void setSeoCode(String seoCode) {
        this.seoCode = seoCode;
    }
}
