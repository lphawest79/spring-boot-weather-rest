package au.com.weather.interceptor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import au.com.weather.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class RateLimitHandleIntereceptorTest {

    @Mock
    RateLimitService rateLimitService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    private RateLimitHandleInterceptor interceptor;

    @Test
    public void testRateLimit() throws Exception {
        when(request.getParameter("apiKey"))
            .thenReturn(null)
            .thenReturn("1")
            .thenReturn("2");

        when(rateLimitService.allowRequest("1"))
            .thenReturn(true);

        when(rateLimitService.allowRequest("2"))
            .thenReturn(false);

        interceptor.preHandle(request, response, null); // success
        interceptor.preHandle(request, response, null); // success

        // failed -- too many requests
        assertThrows(HttpClientErrorException.class, () -> {
            interceptor.preHandle(request, response, null);
        });
    }

}
