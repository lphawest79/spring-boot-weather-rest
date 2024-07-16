package au.com.weather.model.openweathermap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Top level weather info object based on the API call to openweathermap.org
 * At the moment, only the minimum required fields are extrapolated
 */
@Data
@NoArgsConstructor
public class ApiWeatherInfo  extends AbstractApi implements Serializable {

	private String name;
	private String base;
	private long visibility;
	private long dt;
	private ApiSys sys;
	private ApiCoord coord;
	private ApiMain main;
	private ApiWind wind;
	private ApiClouds clouds;
	private long cod;
	
	@JsonProperty("weather")
	private List<ApiWeather> weatherList = new ArrayList<>();
	
	
	
}
