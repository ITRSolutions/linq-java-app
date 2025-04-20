package com.linq.website.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class LogFileCleaner {

//    private static final Logger logger = LoggerFactory.getLogger(LogFileCleaner.class); // Use logger
//    private static final String LOG_FILE_PATH = "logs/application.log";
//
//    @PostConstruct
//    public void clearLogFileIfFirstTime() {
//        try {
//            File logFile = new File(LOG_FILE_PATH);
//
//            if (logFile.exists()) {
//                // Clear the log file
//                FileWriter fileWriter = new FileWriter(logFile, false);
//                fileWriter.write("");
//                fileWriter.close();
//                logger.info("Log file cleared on startup.");
//            } else {
//                // Create the log file if it doesn't exist
//                logFile.createNewFile();
//                logger.info("Log file created.");
//            }
//        } catch (IOException e) {
//            logger.error("Error while clearing log file: " + e.getMessage());
//        }
//    }
}
