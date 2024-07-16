package au.com.weather.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.weather.model.openweathermap.ApiSys;
import au.com.weather.model.openweathermap.ApiWeather;
import au.com.weather.model.openweathermap.ApiWeatherInfo;
import au.com.weather.service.WeatherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@Slf4j
@Testcontainers
@AutoConfigureWebTestClient(timeout = "3600000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class ControllerIntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    WeatherServiceImpl weatherService;

    @Autowired
    WeatherController weatherController;

    private MockWebServer mockWebServer;

    static final String WEATHER_ENDPOINT = "/api/weather";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setupMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        weatherService.setBaseUrl(baseUrl);
        weatherService.setupWebClient();
        mockWebServer.start();
    }

    @Test
    public void testMissingParam() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path(WEATHER_ENDPOINT).queryParam("apiKey", "1234").build())
                .exchange().expectStatus().is4xxClientError();

        webTestClient.get().uri(uriBuilder -> 
            uriBuilder.path(WEATHER_ENDPOINT).queryParam("country", "UK")
                .queryParam("apiKey", "1234").build()).exchange().expectStatus().is4xxClientError();

        webTestClient.get().uri(uriBuilder -> 
            uriBuilder.path(WEATHER_ENDPOINT).queryParam("city", "London")
                .queryParam("apiKey", "1234").build()).exchange().expectStatus().is4xxClientError();
    }

    @Test
    public void testRateLimit() throws Exception {
        ApiWeatherInfo mockApi = new ApiWeatherInfo();
        mockApi.setId(10);
        mockApi.setName("Melbourne");
        mockApi.getWeatherList().add(new ApiWeather("Cloudy with rain", null, null));
        mockApi.setSys(new ApiSys() {
            {
                setCountry("Australia");
            }
        });

        enQueue(mockApi);

        // first call
        String result = callApi("Melbourne", "Australia", "111")
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .returnResult(String.class).getResponseBody()
                .next().block();

        assertEquals(("{\"descriptions\":[\"Cloudy with rain\"]}"), result);

        callApi("Melbourne", "Australia", "111").expectStatus().is2xxSuccessful(); // second call
        callApi("Melbourne", "Australia", "111").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "111").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "111").expectStatus().is2xxSuccessful();
        // 6th call is error - too many limits
        callApi("Melbourne", "Australia", "111").expectStatus().is4xxClientError();

        // lets try different key
        callApi("Melbourne", "Australia", "222").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "222").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "222").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "222").expectStatus().is2xxSuccessful();
        callApi("Melbourne", "Australia", "222").expectStatus().is2xxSuccessful();
        // 6th call is error - too many limits
        callApi("Melbourne", "Australia", "222").expectStatus().is4xxClientError();

    }

    private void enQueue(Object mockApi) throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(mockApi)));
    }

    private ResponseSpec callApi(final String city, final String country, final String apiKey) {
        return webTestClient.get().uri(uriBuilder -> uriBuilder.path(WEATHER_ENDPOINT).queryParam("city", city)
                .queryParam("country", country).queryParam("apiKey", apiKey).build()).exchange();
    }
}
