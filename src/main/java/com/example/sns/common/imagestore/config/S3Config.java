package com.example.sns.common.imagestore.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String endpoint;

    public S3Config(@Value("${ncp.credentials.accessKey}") String accessKey,
                    @Value("${ncp.credentials.secretKey}") String secretKey,
                    @Value("${ncp.region.static}") String region,
                    @Value("${ncp.s3.endpoint}") String endpoint) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.endpoint = endpoint;
    }

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }
}
