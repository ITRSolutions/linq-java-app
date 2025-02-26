package com.linq.website.exceptions;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException(String message) {
        super(message);
    }
}
