package au.com.weather.model.openweathermap;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiWind implements Serializable {

	private double speed;
	private double deg;
	private double gust;
}
