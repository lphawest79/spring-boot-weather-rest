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
public class ApiSys extends AbstractApi implements Serializable {

	private int type;
	private double message;
	private String country;
	private double sunrise;
	private double sunset;
	
}
