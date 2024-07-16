package au.com.weather.model.openweathermap;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiWeather extends AbstractApi implements Serializable {

	private String description;
	private String main;
	private String icon;
	
}
