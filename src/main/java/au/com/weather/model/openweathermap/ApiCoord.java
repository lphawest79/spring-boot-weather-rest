package au.com.weather.model.openweathermap;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiCoord implements Serializable {

	private long lon;
	private long lat;
}
