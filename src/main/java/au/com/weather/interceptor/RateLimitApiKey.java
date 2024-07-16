package au.com.weather.interceptor;

import io.github.bucket4j.Bucket;

public class RateLimitApiKey {

	private String apiKey;
	
	private Bucket bucket;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Bucket getBucket() {
		return bucket;
	}

	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}
}
