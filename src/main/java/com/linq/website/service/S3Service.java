package com.linq.website.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) throws IOException {
        // Validate the file type (optional)
        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File is not an image");
        }

        // Generate a unique filename for the image (e.g., UUID)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Initialize Multipart Upload
        InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest(bucketName, fileName);
        initiateRequest.setObjectMetadata(getMetadata(file));  // Set metadata like content type
        InitiateMultipartUploadResult initiateResponse = amazonS3.initiateMultipartUpload(initiateRequest);
        String uploadId = initiateResponse.getUploadId();

        // Split the file into parts and upload them
        long partSize = 5 * 1024 * 1024; // 5MB is the minimum part size for multipart uploads
        long fileSize = file.getSize();
        long partsCount = (fileSize / partSize) + (fileSize % partSize == 0 ? 0 : 1);

        try {
            // Upload parts
            List<PartETag> partETags = new ArrayList<>();
            for (int i = 0; i < partsCount; i++) {
                long startByte = i * partSize;
                long endByte = Math.min(startByte + partSize, fileSize);

                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(fileName)
                        .withUploadId(uploadId)
                        .withPartNumber(i + 1)
                        .withFileOffset(startByte)
                        .withInputStream(file.getInputStream())
                        .withPartSize(endByte - startByte);

                // Upload part
                UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadRequest);
                partETags.add(uploadPartResult.getPartETag());
            }

            // Complete the Multipart Upload
            CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(bucketName, fileName, uploadId, partETags);
            amazonS3.completeMultipartUpload(completeRequest);

            // Return the S3 URL of the uploaded file
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            // Abort the upload in case of error
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, fileName, uploadId));
            throw new IOException("Error uploading file: " + e.getMessage(), e);
        }
    }

    private ObjectMetadata getMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        return metadata;
    }


    public List<String> getAllFilePaths() {
        List<String> filePaths = new ArrayList<>();
        String nextMarker = null;

        do {
            // List objects in the bucket
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withMarker(nextMarker); // Set the marker for pagination

            var objectListing = amazonS3.listObjects(listObjectsRequest);
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                filePaths.add(amazonS3.getUrl(bucketName, objectSummary.getKey()).toString());
            }
            nextMarker = objectListing.getNextMarker();  // Set the marker for the next set of results

        } while (nextMarker != null);

        return filePaths;
    }
}

