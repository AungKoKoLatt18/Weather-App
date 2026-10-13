package com.example.weather;

import com.example.weather.OpenMeteoModels.ForecastResponse;
import com.example.weather.OpenMeteoModels.GeocodingResponse;
import com.example.weather.OpenMeteoModels.Place;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final String GEO_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestClient client = RestClient.create();

    @Cacheable("weather")
    public WeatherReport getWeather(String city) {
        Place place = geocode(city);
        ForecastResponse f = forecast(place);

        var current = new WeatherReport.Current(
                f.current().temperature(),
                f.current().humidity(),
                f.current().windSpeed(),
                f.current().weatherCode());

        List<WeatherReport.Day> days = new ArrayList<>();
        var d = f.daily();
        for (int i = 0; i < d.time().size(); i++) {
            days.add(new WeatherReport.Day(
                    d.time().get(i), d.maxTemp().get(i), d.minTemp().get(i), d.weatherCode().get(i)));
        }
        return new WeatherReport(place.name(), place.country(), current, days);
    }

    private Place geocode(String city) {
        var uri = UriComponentsBuilder.fromUriString(GEO_URL)
                .queryParam("name", city)
                .queryParam("count", 1)
                .build().encode().toUri();

        GeocodingResponse res = client.get().uri(uri).retrieve().body(GeocodingResponse.class);
        if (res == null || res.results() == null || res.results().isEmpty()) {
            throw new CityNotFoundException(city);
        }
        return res.results().get(0);
    }

    private ForecastResponse forecast(Place p) {
        var uri = UriComponentsBuilder.fromUriString(FORECAST_URL)
                .queryParam("latitude", p.latitude())
                .queryParam("longitude", p.longitude())
                .queryParam("current", "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code")
                .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                .queryParam("timezone", "auto")
                .queryParam("forecast_days", 5)
                .build().toUri();

        return client.get().uri(uri).retrieve().body(ForecastResponse.class);
    }
}
