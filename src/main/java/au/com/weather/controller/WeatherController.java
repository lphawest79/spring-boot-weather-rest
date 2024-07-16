package au.com.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.com.weather.model.dto.WeatherInfoDto;
import au.com.weather.service.WeatherService;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

	@Autowired
	WeatherService weatherService;

	@GetMapping
	public WeatherInfoDto getWeather(@NotBlank @RequestParam final String city,
			@NotBlank @RequestParam final String country,
			@NotBlank @RequestParam final String apiKey) {
		return weatherService.getWeatherInfo(city, country, apiKey);
	}
}
