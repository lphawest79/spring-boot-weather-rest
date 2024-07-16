package au.com.weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import au.com.weather.model.dto.WeatherErrorCode;

@ControllerAdvice
public class WeatherControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { HttpClientErrorException.class })
	public ResponseEntity<WeatherErrorCode> handleClientErrorException(HttpClientErrorException ex, WebRequest request) {
		
		if (HttpStatus.TOO_MANY_REQUESTS.equals(ex.getStatusCode())) {
			return new ResponseEntity<WeatherErrorCode>(
					new WeatherErrorCode("Too many requests", "Error", HttpStatus.TOO_MANY_REQUESTS.value()),
					HttpStatus.TOO_MANY_REQUESTS); 
		}
		
		return new ResponseEntity<WeatherErrorCode>(
				new WeatherErrorCode("Invalid input", "Error", HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	public ResponseEntity<WeatherErrorCode> handleMissingParameter(MissingServletRequestParameterException ex, WebRequest request) {
		
		return new ResponseEntity<WeatherErrorCode>(
				new WeatherErrorCode("Missing parameter: " + ex.getParameterName(), "Error", HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = { WebClientResponseException.Unauthorized.class })
	public ResponseEntity<WeatherErrorCode> handleUnauthorized(WebClientResponseException.Unauthorized ex, WebRequest request) {
		
		return new ResponseEntity<WeatherErrorCode>(
				new WeatherErrorCode("Invalid ApiKey", "Error", HttpStatus.UNAUTHORIZED.value()),
				HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = { WebClientResponseException.NotFound.class })
	public ResponseEntity<WeatherErrorCode> handleNotFound(WebClientResponseException.NotFound ex, WebRequest request) {
		
		return new ResponseEntity<WeatherErrorCode>(
				new WeatherErrorCode("Record Not Found", "Error", HttpStatus.NOT_FOUND.value()),
				HttpStatus.NOT_FOUND);
	}	
	
	
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<WeatherErrorCode> handleDefault(Exception ex, WebRequest request) {
		
		return new ResponseEntity<WeatherErrorCode>(
				new WeatherErrorCode("Internal Server Error: " + ex.getMessage(), "Error",  HttpStatus.INTERNAL_SERVER_ERROR.value()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
