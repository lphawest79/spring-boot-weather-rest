package au.com.weather.model.openweathermap;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiClouds implements Serializable {

	private double all;
}
