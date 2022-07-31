package com.thomasBonan.weather.utilities;

public class Api {
    private static final String FORECAST_API_BASE_URL= "https://api.ambeedata.com/weather/latest/by-lat-lng?lat=";

    public static String getForecastURL(double latitude, double longitude){
        return FORECAST_API_BASE_URL+latitude +"&lng="+longitude+"&units=si";

    }

    private static final String FORECAST_API_SECOND_URL = "https://api.ipgeolocation.io/timezone?apiKey=";

    public static String getSecondURL (String ApiKey, double latitude,double longitude){
        return FORECAST_API_SECOND_URL+ApiKey+"&lat="+latitude+"&long="+longitude;
    }

}
