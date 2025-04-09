package com.linq.website.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogFileEndpoint {

    private static final String LOG_FILE_PATH = "logs/application.log";

    @GetMapping
    public ResponseEntity<Resource> viewLogFile() {
        try {
            // Read the lines of the log file
            File logFile = new File(LOG_FILE_PATH);
            if (!logFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Read all lines from the log file
            List<String> lines = Files.readAllLines(Paths.get(LOG_FILE_PATH));

            // Reverse the lines so the latest log messages come first
            Collections.reverse(lines);

            // Join the lines back into a single string
            StringBuilder reversedLogContent = new StringBuilder();
            for (String line : lines) {
                reversedLogContent.append(line).append("\n");
            }

            // Convert the reversed log content to an InputStreamResource
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(reversedLogContent.toString().getBytes()));

            return ResponseEntity.ok()
                    .contentLength(reversedLogContent.length())
                    .contentType(MediaType.TEXT_PLAIN) // Sets content type as plain text
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
