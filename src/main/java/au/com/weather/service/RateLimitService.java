package au.com.weather.service;

public interface RateLimitService {

	public boolean allowRequest(String apiKey);
}
