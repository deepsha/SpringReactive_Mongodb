package com.shop.app.products.inventoryservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(ProductAPIexception.class)
	public ResponseEntity<?> handleProductExcpetion(ProductAPIexception exception)
	{
		Map<String,String> errormap=new HashMap<>();
		errormap.put("error message",exception.getMessage());
		errormap.put("status",HttpStatus.BAD_REQUEST.toString());
		return ResponseEntity.ok(errormap);
				
	}
}
