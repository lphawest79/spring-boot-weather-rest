package au.com.weather.model.openweathermap;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiMain implements Serializable {

	private double temp;
	private double feels_like;
	private long pressure;
	private int humidity; 
	private double temp_min;
	private double temp_max;
	private double sea_level;
	private double grnd_level;
}
