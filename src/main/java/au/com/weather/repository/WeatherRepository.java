package au.com.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.weather.model.entity.WeatherInfo;

public interface WeatherRepository extends JpaRepository<WeatherInfo, Long> {

	WeatherInfo findOneByCityAndCountry(final String city, final String country);
}
