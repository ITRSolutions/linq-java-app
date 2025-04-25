package com.linq.website.utility;

import org.springframework.stereotype.Component;

@Component("webUtils")
public class ThymeleafUtils {

    public String createLink(String value) {
        if (value.trim().isEmpty()) {
            return "#";
        }
        value = value.replaceAll("\\s*\\([^)]*\\)", "");               // Remove parentheses and content inside
        value = value.replaceAll("[^a-zA-Z0-9\\s-]", "");              // Remove special characters except space and dash
        value = value.trim();                                         // Trim leading/trailing spaces
        String link = value.replaceAll("\\s+", "-")                   // Replace one or more spaces with dash
                .replaceAll("-+", "-")                    // Collapse multiple dashes
                .toLowerCase();                           // Convert to lowercase

        return link;
    }

    public String validateString(String value) {
        value = value.trim();
        if (value == null || value.isEmpty()) {
            return "<span style='color: red'>Field is empty<span>";
        }
        return value;
    }
}