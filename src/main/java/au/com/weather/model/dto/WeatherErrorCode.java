package au.com.weather.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherErrorCode {

	private String message;
	private String status;
	private int code;
}
