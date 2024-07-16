package au.com.weather.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import au.com.weather.model.dto.WeatherErrorCode;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerAdviceTest {

    private WeatherControllerAdvice controllerAdvice = new WeatherControllerAdvice();

    @Mock
    WebRequest webRequest;

    @Test
    public void testHandleClientErrorException() {
        ResponseEntity<WeatherErrorCode> err = controllerAdvice
                .handleClientErrorException(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS), webRequest);

        assertEquals("Error", err.getBody().getStatus());
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), err.getBody().getCode());

        // any other error should be bad request
        err = controllerAdvice.handleClientErrorException(new HttpClientErrorException(HttpStatus.BAD_GATEWAY),
                webRequest);
        assertEquals("Error", err.getBody().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), err.getBody().getCode());
    }

    @Test
    public void testHandleMissingParameterException() {
        ResponseEntity<WeatherErrorCode> err = controllerAdvice
                .handleMissingParameter(new MissingServletRequestParameterException("city", "String"), webRequest);
        assertEquals("Error", err.getBody().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), err.getBody().getCode());
    }

    @Test
    public void testHandleDefault() {
        ResponseEntity<WeatherErrorCode> err = controllerAdvice.handleDefault(new RuntimeException("Failed to load"),
                webRequest);
        assertEquals("Error", err.getBody().getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), err.getBody().getCode());
    }
}
