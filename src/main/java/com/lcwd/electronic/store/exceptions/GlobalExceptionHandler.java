package com.lcwd.electronic.store.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.lcwd.electronic.store.helper.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


//	ResourceNotFoundException Handler
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
		
		log.info("ResourceNotFoundException Invoked..!!");
		ApiResponseMessage build = ApiResponseMessage.builder().message(exception.getMessage())
				.status(HttpStatus.NOT_FOUND).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(build, HttpStatus.OK);
	}

//	MethodArgumentNotValidException Handler
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException exception) {
		log.info("MethodArgumentNotValidException Invoked..!!");
		

		List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
		Map<String, Object> response = new HashMap<>();
		allErrors.stream().forEach(objectError -> {
			String message = objectError.getDefaultMessage();
			String field = ((FieldError) objectError).getField();
			response.put(field, message);
		});

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

//	PropertyReferenceException handler (when we sort the name wise and if write property name (not particulary name it can be any field) wrong then exception will occur..)

	@ExceptionHandler(PropertyReferenceException.class)	
	public ResponseEntity<ApiResponseMessage> propertyReferenceExceptionHandler(PropertyReferenceException exception) {
			log.info("PropertyReferenceException Invoked..!!");
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message(exception.getMessage())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).success(true).build();

		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	BadApiRequest handler
	@ExceptionHandler(BadApiRequest.class)
	public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequest exception) {
			log.info("BadApiRequest Invoked..!!");
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message(exception.getMessage())
				.status(HttpStatus.BAD_REQUEST).success(false).build();

		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.BAD_REQUEST);
	}

	/*
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseMessage> getException(Exception exception) {
		log.info("Universal Exception Invoked..!!");
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message(exception.toString())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
	

}
