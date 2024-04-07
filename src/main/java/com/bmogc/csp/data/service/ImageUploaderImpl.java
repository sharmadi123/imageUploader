package com.bmogc.csp.data.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bmogc.csp.data.exception.ImageUploadException;

@Service
public class ImageUploaderImpl implements ImageUploader {

	@Autowired
	private AmazonS3 client;

	@Value("${app.s3.bucket}")
	private String bucketName;

	@Override
	public String uploadImage(MultipartFile file) {

		// abc.png
		String actualFileName = file.getOriginalFilename();

		// id+ .png
		String fileName = UUID.randomUUID().toString() + actualFileName.substring(actualFileName.lastIndexOf("."));

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.getInstanceLength();
		try {
			PutObjectResult putObjectResult = client
					.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metaData));
			return this.preSignedUrl(fileName);
		} catch (IOException e) {

			throw new ImageUploadException("error in uploading image" + e.getMessage());
		}

	}

	@Override
	public List<String> allFiles() {
		
		ListObjectsV2Result listObjectV2Result=client.listObjectsV2(new ListObjectsV2Request().withBucketName(bucketName));
		List<S3ObjectSummary> objectSummaries= listObjectV2Result.getObjectSummaries();
		List<String> listFilesUrl=objectSummaries.stream().map(item -> this.preSignedUrl(item.getKey())).collect(Collectors.toList());
		
		return listFilesUrl;
	}

	//filename jo authorized aws user ke paas hai, need to access using presigend url
	@Override
	public String preSignedUrl(String filename) {
		Date expirationDate=new Date();
		long time =expirationDate.getTime();
		int hour=2;
		//converting into miliseconds
		time=time+hour*60*60*1000; 
		
		//abhi ke 2 ghante baad vala time
		expirationDate.setTime(time);
		
		GeneratePresignedUrlRequest  generatePresignedUrlRequest=new GeneratePresignedUrlRequest(bucketName,filename)
				.withMethod(HttpMethod.GET)
				.withExpiration(expirationDate);
		
		URL url=client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}
	
	public String getImageUrlByFileName(String fileName)
	{
		S3Object object =client.getObject(bucketName,fileName);
		//file ka naam jo s3 me hai
		String key=object.getKey();
		String url=preSignedUrl(key);
		return url;
		
	}

}
