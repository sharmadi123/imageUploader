package com.bmogc.csp.data.exception;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.services.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	//need to understand this from code
	@ExceptionHandler
	public String handleImageUploadException(ImageUploadException imageUploadException){
		return imageUploadException.getMessage();
	}
}
