package com.example.weather;

import java.util.List;

/** The clean response our API returns to the frontend. */
public record WeatherReport(
        String city,
        String country,
        Current current,
        List<Day> forecast) {

    public record Current(double temperature, int humidity, double windSpeed, int weatherCode) {}

    public record Day(String date, double maxTemp, double minTemp, int weatherCode) {}
}
