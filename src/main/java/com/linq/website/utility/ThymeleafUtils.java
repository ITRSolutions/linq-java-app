package com.linq.website.utility;

import org.springframework.stereotype.Component;

@Component("webUtils")
public class ThymeleafUtils {

    public String createLink(String value) {
        String link = value.replace(" ", "-").toLowerCase();
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
