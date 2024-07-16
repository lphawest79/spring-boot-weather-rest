package au.com.weather.model.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import au.com.weather.model.openweathermap.ApiWeatherInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity Weather Info object that stores the json object obstained from
 * openweathermap. Additionally, city and country attributes are added in to
 * easily search for the records
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "WEATHER_INFO")
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String country;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private ApiWeatherInfo json;

    public WeatherInfo(final String city, final String country, final ApiWeatherInfo json) {
        this.city = city;
        this.country = country;
        this.json = json;
    }
}
