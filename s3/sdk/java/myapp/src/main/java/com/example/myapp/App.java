package com.example.myapp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Lambda function entry point with a main method for local testing.
 */
public class App implements RequestHandler<Object, Object> {
    private final S3AsyncClient s3Client;

    public App() {
        // Initialize the SDK client
        s3Client = DependencyFactory.s3Client();
    }

    @Override
    public Object handleRequest(final Object input, final Context context) {
        String bucketName = "my-unique-bucket-" + System.currentTimeMillis();

        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        CompletableFuture<CreateBucketResponse> response = s3Client.createBucket(createBucketRequest);

        try {
            // Wait for the response (asynchronously)
            CreateBucketResponse createBucketResponse = response.join();
            if (context != null) {
                context.getLogger().log("Bucket created successfully: " + createBucketResponse.location());
            } else {
                System.out.println("Bucket created successfully: " + createBucketResponse.location());
            }
        } catch (Exception e) {
            if (context != null) {
                context.getLogger().log("Failed to create bucket: " + e.getMessage());
            } else {
                System.out.println("Failed to create bucket: " + e.getMessage());
            }
        }

        return "Bucket creation request processed.";
    }
}
