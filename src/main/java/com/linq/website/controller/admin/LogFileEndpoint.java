package com.linq.website.controller.admin;

import jakarta.annotation.security.PermitAll;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/api/v1/logs")
@PermitAll
public class LogFileEndpoint {

    private static final String LOG_FILE_PATH = "logs/application.log";

    @GetMapping
    public ResponseEntity<Resource> viewLogFile() {
        try {
            File logFile = new File(LOG_FILE_PATH);
            if (!logFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(logFile));
            return ResponseEntity.ok()
                    .contentLength(logFile.length())
                    .contentType(MediaType.TEXT_PLAIN) // Makes browser display it
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
