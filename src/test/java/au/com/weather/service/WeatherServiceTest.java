package au.com.weather.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import au.com.weather.model.entity.WeatherInfo;
import au.com.weather.model.openweathermap.ApiWeatherInfo;
import au.com.weather.repository.WeatherRepository;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {
	
	@Mock
	WeatherRepository weatherRepository;
	
	@Mock
	WeatherInfo weatherInfo;
	
	@Mock
	ApiWeatherInfo apiWeatherInfo;
	
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	WebClient webClient;
	
	@InjectMocks
	private WeatherServiceImpl weatherService;
	
	
	@Test
	public void testGetWeatherInfo() {
		when(weatherRepository.findOneByCityAndCountry("London", "UK"))
			.thenReturn(null)
			.thenReturn(weatherInfo);
		
		// first call should call the rest api save in the database
		assertNotNull(weatherService.getWeatherInfo("London", "UK", "12345"));
		verify(weatherRepository, times(1)).save(any(WeatherInfo.class));
		verify(webClient, times(1)).get();
				
		// second call should NOT call the rest api or save in database
		assertNotNull(weatherService.getWeatherInfo("London", "UK", "12345"));
		verify(weatherRepository, times(1)).save(any(WeatherInfo.class));
		verify(webClient, times(1)).get();
	}
	
	@Test
	public void testGetWeatherInfo_invokeApiError() {
		 when(webClient.get()
                 .uri(any(Function.class))
                 .retrieve()
                 .bodyToMono(ApiWeatherInfo.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		 
		 assertThrows(HttpClientErrorException.class, 
				 () -> {
					 weatherService.getWeatherInfo("London", "UK", "12345");
				 });
	}

}
