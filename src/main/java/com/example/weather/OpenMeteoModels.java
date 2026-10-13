package com.example.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Raw response shapes from the Open-Meteo APIs. */
final class OpenMeteoModels {
    private OpenMeteoModels() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GeocodingResponse(List<Place> results) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Place(String name, String country, double latitude, double longitude) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ForecastResponse(Current current, Daily daily) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Current(
            @JsonProperty("temperature_2m") double temperature,
            @JsonProperty("relative_humidity_2m") int humidity,
            @JsonProperty("wind_speed_10m") double windSpeed,
            @JsonProperty("weather_code") int weatherCode) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Daily(
            List<String> time,
            @JsonProperty("temperature_2m_max") List<Double> maxTemp,
            @JsonProperty("temperature_2m_min") List<Double> minTemp,
            @JsonProperty("weather_code") List<Integer> weatherCode) {}
}
