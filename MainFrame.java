package com.thomasBonan.weather;

import com.thomasBonan.weather.models.CurrentWeather;
import com.thomasBonan.weather.utilities.Alert;
import com.thomasBonan.weather.utilities.Api;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainFrame extends JFrame {

    CurrentWeather currentWeather;

    private static final Color BLUE_COLOR = Color.decode("#8EA2C6");
    private static final Color WHITE_COLOR = Color.white;
    private static final Color LIGHT_GREY_COLOR = new Color(255,255,255,150);
    private static final Font DEFAULT_FONT = new Font("Proxima Nova",Font.PLAIN, 24);

    private JLabel temperatureLabel;
    private JLabel timeLabel;
    private JLabel locationLabel;
    private JPanel otherInfoPanel;
    private JLabel humidityLabel;
    private JLabel humidityValue;
    private JLabel windSpeedLabel;
    private JLabel windSpeedValue;
    private JLabel summaryLabel;


    public MainFrame(String title) {
        super(title);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        contentPane.setBackground(BLUE_COLOR);

        temperatureLabel = new JLabel("--",SwingConstants.CENTER);
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperatureLabel.setForeground(WHITE_COLOR);
        temperatureLabel.setFont(DEFAULT_FONT.deriveFont(160f));

        timeLabel = new JLabel("--",SwingConstants.CENTER);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(WHITE_COLOR);
        timeLabel.setForeground(LIGHT_GREY_COLOR);
        timeLabel.setFont(DEFAULT_FONT.deriveFont(18f));

        locationLabel = new JLabel("Paris, FR", SwingConstants.CENTER);
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLabel.setForeground(WHITE_COLOR);
        locationLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        locationLabel.setFont(DEFAULT_FONT);

        summaryLabel = new JLabel("Récupération de la température actuelle...",SwingConstants.CENTER);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryLabel.setForeground(WHITE_COLOR);
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        summaryLabel.setFont(DEFAULT_FONT.deriveFont(14f));

        otherInfoPanel = new JPanel();
        otherInfoPanel.setLayout(new GridLayout(2,2));
        otherInfoPanel.setBackground(BLUE_COLOR);

        humidityLabel = new JLabel("Humidité".toUpperCase(),SwingConstants.CENTER);
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityLabel.setForeground(LIGHT_GREY_COLOR);
        humidityLabel.setFont(DEFAULT_FONT.deriveFont(12f));

        humidityValue = new JLabel("--",SwingConstants.CENTER);
        humidityValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityValue.setForeground(WHITE_COLOR);
        humidityValue.setFont(DEFAULT_FONT);

        windSpeedLabel = new JLabel("Vitesse du vent".toUpperCase(),SwingConstants.CENTER);
        windSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        windSpeedLabel.setForeground(LIGHT_GREY_COLOR);
        windSpeedLabel.setFont(DEFAULT_FONT.deriveFont(12f));

        windSpeedValue = new JLabel("--",SwingConstants.CENTER);
        windSpeedValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        windSpeedValue.setForeground(WHITE_COLOR);
        windSpeedValue.setFont(DEFAULT_FONT);

        otherInfoPanel.add(humidityLabel);
        otherInfoPanel.add(windSpeedLabel);
        otherInfoPanel.add(humidityValue);
        otherInfoPanel.add(windSpeedValue);

        contentPane.add(locationLabel);
        contentPane.add(timeLabel);
        contentPane.add(temperatureLabel);
        contentPane.add(otherInfoPanel);
        contentPane.add(summaryLabel);

        setContentPane(contentPane);

        double latitude = 37;
        double longitude = -122;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Api.getForecastURL(latitude, longitude))
                .get()
                .addHeader("x-api-key", "280a33adcdcc9e57cde70a4948d3845df9e63ba849b0cf95aa0e61c874c8e1b7")
                .addHeader("Content-type","application/json")
                .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Alert.error(MainFrame.this,"Erreur","Oops, une erreur est survenue." );
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();

                            OkHttpClient client2 = new OkHttpClient();
                            String ApiKey = "5a0fa4a5e3644b0f84220b5b5825b9fa";
                            Request request2 = new Request.Builder()
                                    .url(Api.getSecondURL(ApiKey,latitude,longitude))
                                    .get()
                                    .build();
                                    Call call2 = client2.newCall((request2));
                                    call2.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Alert.error(MainFrame.this,"Erreur","Oops, une erreur est survenue." );
                                }

                                @Override
                                public void onResponse(Call call2, Response response2) throws IOException {
                                    String jsonData2 = response2.body().string();
                                    currentWeather =getCurrentWeatherDetails(jsonData,jsonData2);
                                    updateScreen();
                                }
                            });



                        } else {
                            Alert.error(MainFrame.this,"Erreur","Oops, une erreur est survenue." );
                            System.err.println("Error : " + response.body().string());
                        }
                    }
                });
        }

    private void updateScreen() {
        timeLabel.setText("Il est "+currentWeather.getFormattedTime()+" et la température est de :");
        temperatureLabel.setText(currentWeather.getTemperature()+"°");
        humidityValue.setText(currentWeather.getHumidity()+"");
        windSpeedValue.setText(currentWeather.getWindSpeed()+"");
        summaryLabel.setText(currentWeather.getSummary());

    }

    private CurrentWeather getCurrentWeatherDetails(String jsonData,String jsonData2) {
        CurrentWeather currentWeather = new CurrentWeather();
        JSONObject forecast = (JSONObject) JSONValue.parse(jsonData);
        JSONObject data = (JSONObject) forecast.get("data");
        currentWeather.setHumidity(Double.parseDouble(data.get("humidity")+""));
        currentWeather.setSummary((String) data.get("summary"));
        currentWeather.setTemperature(Double.parseDouble(data.get("temperature")+""));
        currentWeather.setTime((long) data.get("time"));
        currentWeather.setWindSpeed(Double.parseDouble(data.get("windSpeed")+""));

        JSONObject forecast2 = (JSONObject) JSONValue.parse(jsonData2);
        currentWeather.setTimezone((String) forecast2.get("timezone"));

        return currentWeather;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,500);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
