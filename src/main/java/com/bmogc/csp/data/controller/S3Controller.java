package com.bmogc.csp.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bmogc.csp.data.service.ImageUploaderImpl;

@RestController
@RequestMapping(value = "/api/v1/s3")
//need to understand security and allowing access to apis form project
public class S3Controller {

	@Autowired
	ImageUploaderImpl service;

	// upload image
	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
		return ResponseEntity.ok(service.uploadImage(file));
	}

	// get all files url
	@GetMapping
	public List<String> getImages() {
		return service.allFiles();
	}

	// get image url by file name
	@GetMapping("/{fileName}")
	public String getImageUrlByFileName(@PathVariable(value = "fileName") String fileName) {
		return service.getImageUrlByFileName(fileName);
	}

}
