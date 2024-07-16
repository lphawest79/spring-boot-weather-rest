package au.com.weather.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.weather.model.entity.WeatherInfo;
import lombok.Data;

@Data
public class WeatherInfoDto {

	@JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();
	
	private List<String> descriptions = new ArrayList<>();
	
	public WeatherInfoDto(final WeatherInfo weatherInfo) {
		if (weatherInfo != null && weatherInfo.getJson() != null) {
			weatherInfo.getJson().getWeatherList().forEach(i -> {
				descriptions.add(i.getDescription());
			});
		}
	}
}
