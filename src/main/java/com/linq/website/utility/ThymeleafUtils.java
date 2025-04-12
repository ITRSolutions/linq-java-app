package com.linq.website.utility;

import org.springframework.stereotype.Component;

@Component("webUtils")
public class ThymeleafUtils {

    public String createLink(String value) {
        value = value.trim();
        String link = value.replaceAll("[^a-zA-Z0-9 ]", "")
                .replace(" ", "-").replace("--", "-").toLowerCase();
        if (link.isEmpty()) {
            return "#";
        }
        return link;
    }

    public String validateString(String value) {
        if (value == null || value.isEmpty()) {
            return "<span style='color: red'>Field is empty<span>";
        }
        return value;
    }
}