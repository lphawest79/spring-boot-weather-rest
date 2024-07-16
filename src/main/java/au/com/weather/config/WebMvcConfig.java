package au.com.weather.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import au.com.weather.interceptor.RateLimitHandleInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    RateLimitHandleInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
    }
}
