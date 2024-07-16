package au.com.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import au.com.weather.model.dto.WeatherInfoDto;
import au.com.weather.model.entity.WeatherInfo;
import au.com.weather.model.openweathermap.ApiWeatherInfo;
import au.com.weather.repository.WeatherRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import reactor.core.publisher.Mono;

@Service
@Data
public class WeatherServiceImpl implements WeatherService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Value("${weather.api.base}")
	private String baseUrl;
	
	@Value("${weather.api.weather-path}")
	private String weatherPath;
	
    WebClient webClient = null;
    
    @Autowired
    private WeatherRepository weatherRepository;
    
    @PostConstruct
    public void setupWebClient() {
    	webClient = WebClient.builder().baseUrl(baseUrl).build();
    }
    
    @Transactional
    public WeatherInfoDto getWeatherInfo(final String city, final String country, final String apiKey) {
    	// firstly retrieve from storage
    	WeatherInfo weatherInfo = weatherRepository.findOneByCityAndCountry(city, country);
    	
    	if (weatherInfo == null) {
    		// invoke api and save
    		final ApiWeatherInfo result = invokeOpenWeatherMapApi(city, country, apiKey);
    		weatherInfo = save(city, country, result);
    	}
    	return new WeatherInfoDto(weatherInfo);
    }
	
    private WeatherInfo save(final String city, final String country, final ApiWeatherInfo json) {
    	WeatherInfo weatherInfo = new WeatherInfo(city, country, json);
		weatherInfo = weatherRepository.save(weatherInfo);
		logger.info("Successfully saved record {}", weatherInfo);
		return weatherInfo;
    }
	
    private ApiWeatherInfo invokeOpenWeatherMapApi(final String city, final String country,
    		final String apiKey) {
    	Mono<ApiWeatherInfo> result = this.webClient
				.get()
				.uri(builder -> builder.path(weatherPath)
						 .queryParam("q", city + "," + country)
						 .queryParam("appid", apiKey)
						 .build())
				.retrieve()
				.bodyToMono(ApiWeatherInfo.class);
		
    	ApiWeatherInfo info = result.block();
 		logger.info("Successfully invoked rest api  {}", baseUrl + weatherPath);
 		
 		logger.info("Result: {}", info);
 		return info;
    }
}
