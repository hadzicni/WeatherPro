package com.hadzicni;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends Application {

    private static final String API_KEY = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextField cityInput = new TextField();
        Button getWeatherButton = new Button("Wetter abrufen");
        Label weatherLabel = new Label();

        getWeatherButton.setOnAction(e -> {
            String city = cityInput.getText();
            String weatherData = getWeatherData(city);

            if (weatherData != null) {
                displayWeather(weatherData, weatherLabel, city);
            } else {
                weatherLabel.setText("Stadt nicht gefunden.");
            }
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(new Label("Stadt eingeben:"), cityInput, getWeatherButton, weatherLabel);
        root.setStyle("-fx-padding: 10px");

        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setTitle("Wetter-App");
        primaryStage.show();
    }

    private static String getWeatherData(String city) {
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY
                + "&units=metric";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.toString();
    }

    private static void displayWeather(String weatherData, Label weatherLabel, String city) {
        String description = weatherData.split("\"description\":\"")[1].split("\"")[0];
        String temperature = weatherData.split("\"temp\":")[1].split(",")[0];
        String humidity = weatherData.split("\"humidity\":")[1].split(",")[0];
        String windSpeed = weatherData.split("\"speed\":")[1].split(",")[0];

        String output = String.format(
                "Aktuelles Wetter in %s:\nBeschreibung: %s\nTemperatur: %s°C\nLuftfeuchtigkeit: %s%%\nWindgeschwindigkeit: %s m/s",
                city, description, temperature, humidity, windSpeed);

        weatherLabel.setText(output);
    }
}
