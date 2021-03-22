package com.app.bookeepy.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.app.bookeepy.exceptions.BookAlreadyExistsException;
import com.app.bookeepy.exceptions.BookException;
import com.app.bookeepy.exceptions.BookNotFoundException;
import com.app.bookeepy.exceptions.ImageException;
import com.app.bookeepy.exceptions.ImageNotFoundException;
import com.app.bookeepy.exceptions.InvalidBookStatusException;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidBookStatusException.class)
	public ResponseEntity<Object> handleInvalidBookStatusException(InvalidBookStatusException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BookException.class)
	public ResponseEntity<Object> handleInvalidBookException(BookException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<Object> handleImageException(ImageException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BookAlreadyExistsException.class)
	public ResponseEntity<Object> handleAlreadyExistsException(BookAlreadyExistsException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<Object> handleImageNotFoundException(ImageNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", "fail");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new LinkedHashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return new ResponseEntity<>(errors, headers, HttpStatus.BAD_REQUEST);

	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> errors = new LinkedHashMap<String, Object>();
		errors.put("timestamp", LocalDateTime.now());
		errors.put("message", ex.getMethod() + " method is not supported for this operation!");
		errors.put("supported", ex.getSupportedHttpMethods().stream().map(method -> new String(method.toString())));

		return new ResponseEntity<Object>(errors, headers, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		Map<String, String> response = new LinkedHashMap<String, String>();
		response.put("status", "fail");
		response.put("message", ex.getLocalizedMessage());
		response.put("Method", ex.getHttpMethod());

		return new ResponseEntity<Object>(response, headers, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleNumberFormatException(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", "fail");
		response.put("message", "Please specify a valid " + ex.getName() + "!");

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
