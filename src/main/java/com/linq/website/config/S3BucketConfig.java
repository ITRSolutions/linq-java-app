package com.linq.website.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3BucketConfig {

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        // Check if credentials and region are injected correctly
        if (awsAccessKey == null || awsSecretKey == null || region == null) {
            throw new IllegalArgumentException("AWS credentials or region not found in application properties");
        }

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        Regions awsRegion = Regions.fromName(region); // Convert string region to AWS Regions enum

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion)
                .build();
    }
}