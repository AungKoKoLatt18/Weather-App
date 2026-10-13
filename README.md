# 🌤️ Weather App

A simple web application built with **Java** and **Spring Boot** that shows the current weather and a 5-day forecast for any city. Weather data comes from the free [Open-Meteo](https://open-meteo.com/) API, so no API key is needed.



## Features

- Search any city in the world
- Current temperature, humidity and wind speed
- 5-day forecast with weather icons
- In-memory caching to avoid repeated API calls
- Clear error messages (city not found, service unavailable)
- Responsive page with automatic dark mode

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Language (records, modern syntax) |
| Spring Boot 3 | Web framework with embedded Tomcat |
| Maven | Build and dependency management |
| Open-Meteo API | Geocoding and weather data |
| HTML / CSS / JavaScript | Frontend, served by Spring Boot |

## How It Works

```
Browser → WeatherController → WeatherService → Open-Meteo API
                                   │
                                   └── result cached in memory
```

1. The browser calls `GET /api/weather?city=Ankara`.
2. The controller validates the input.
3. The service converts the city name to coordinates (geocoding API), then fetches the forecast.
4. The result is converted into a clean JSON response and cached.

## Project Structure

```
weather-app
├── pom.xml
└── src/main
    ├── java/com/example/weather
    │   ├── WeatherApplication.java      # entry point
    │   ├── WeatherController.java       # REST endpoint + error handling
    │   ├── WeatherService.java          # calls Open-Meteo, builds the result
    │   ├── WeatherReport.java           # response sent to the frontend
    │   ├── OpenMeteoModels.java         # shapes of the external API's JSON
    │   ├── CityNotFoundException.java
    │   └── CacheConfig.java             # enables caching
    └── resources
        ├── application.properties
        └── static/index.html            # frontend
```

## Getting Started

### Prerequisites

- JDK 17 or newer (`java -version`)
- Maven 3.6+ (`mvn -v`)

### Run

```bash
git clone https://github.com/AungKoKoLatt18/weather-app.git
cd weather-app
mvn spring-boot:run
```

Then open **http://localhost:8080** in your browser.

### Build a runnable jar

```bash
mvn package
java -jar target/weather-app-1.0.0.jar
```

## API

### `GET /api/weather?city={name}`

Example: `http://localhost:8080/api/weather?city=Ankara`

```json
{
  "city": "Ankara",
  "country": "Turkey",
  "current": {
    "temperature": 18.2,
    "humidity": 55,
    "windSpeed": 9.4,
    "weatherCode": 2
  },
  "forecast": [
    { "date": "2026-10-13", "maxTemp": 21.0, "minTemp": 10.0, "weatherCode": 3 }
  ]
}
```

(The values above are an illustration of the format.)

| Status | Meaning |
|---|---|
| 200 | Success |
| 400 | Empty or invalid city |
| 404 | City not found |
| 502 | Open-Meteo is unavailable |

## Known Limitations

- The cache never expires, so data can get stale while the server runs.
- Only the best geocoding match is used (ambiguous names may resolve to the wrong city).
- No automated tests yet.
- Open-Meteo is free for non-commercial use only.

## Roadmap

- [ ] Cache expiry (Caffeine)
- [ ] Unit and integration tests
- [ ] °C / °F toggle
- [ ] Hourly forecast
- [ ] Favorite cities with a database
- [ ] Dockerfile

## Acknowledgements

Weather data by [Open-Meteo](https://open-meteo.com/).

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
