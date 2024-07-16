package au.com.weather.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;

import au.com.weather.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitHandleInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        final String apiKey = request.getParameter("apiKey");

        if (apiKey != null && !rateLimitService.allowRequest(apiKey)) {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
        return true;
    }

}
