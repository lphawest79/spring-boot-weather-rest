package au.com.weather.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class RateLimitServiceTest {

	
	RateLimitServiceImpl rateLimitService = new RateLimitServiceImpl();
	
	
	@Test
	public void testAllowRequest() {
		// configure to allow x3 calls every minute for each api key
		rateLimitService.callLimit = 2;
		rateLimitService.duration = 1;
		
		assertTrue(rateLimitService.allowRequest("1234"));
		assertTrue(rateLimitService.allowRequest("1234"));
		assertFalse(rateLimitService.allowRequest("1234"));
		
		assertTrue(rateLimitService.allowRequest("4567"));
		assertTrue(rateLimitService.allowRequest("4567"));
		assertFalse(rateLimitService.allowRequest("4567"));
		
		assertTrue(rateLimitService.allowRequest("8910"));
		assertTrue(rateLimitService.allowRequest("8910"));
		assertFalse(rateLimitService.allowRequest("8910"));
	}
	
}
