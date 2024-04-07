package com.bmogc.csp.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.*;

@Configuration
public class S3config {
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String awsSecretKey;
	
	@Value("${cloud.aws.credentials.access-key}")
	private String awsAccessKey;
	
	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public AmazonS3 client() {
		
		AWSCredentials credentials =new BasicAWSCredentials(awsAccessKey,awsSecretKey);
		AmazonS3 amazonS3=AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
		.withRegion(region)
		.build();
		return amazonS3;
		
	}
}
