package au.com.weather.service;

import au.com.weather.model.dto.WeatherInfoDto;

public interface WeatherService {

	WeatherInfoDto getWeatherInfo(final String city, final String country, final String apiKey);
	 
}
