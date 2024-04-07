package com.bmogc.csp.data.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
	
	String uploadImage(MultipartFile file);
	
	List<String> allFiles();
	
	String preSignedUrl(String filename);
	
	String getImageUrlByFileName(String fileName);
}
