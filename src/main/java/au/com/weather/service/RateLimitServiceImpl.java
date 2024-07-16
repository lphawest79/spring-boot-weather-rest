package au.com.weather.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitServiceImpl implements RateLimitService {

	
	@Value("${weather.api.call-limit}")
	int callLimit;
	
	@Value("${weather.api.duration}")
	int duration;
	
	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	@Override
    public boolean allowRequest(String apiKey) {
        Bucket bucket = buckets.computeIfAbsent(apiKey, this::createNewBucket);
        return bucket.tryConsume(1);
    }

    private Bucket createNewBucket(String apiKey) {
        Bandwidth limit = Bandwidth.simple(callLimit, Duration.ofMinutes(duration));
        
        return Bucket.builder().addLimit(limit).build();
    }
    
}
