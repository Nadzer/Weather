package com.weather.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nadzer on 23/05/2016.
 */
public class WeatherParser {
    private String data;
    private ArrayList<Weather> mWeathers;

    public WeatherParser(String data) {
        this.data = data;
        mWeathers = new ArrayList<>();
        parsingData();
    }

    private void parsingData() {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            String city = jsonObject.getJSONObject("city").getString("name");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dayForecast = jsonArray.getJSONObject(i);
                Weather weatherForecast = new Weather();
                weatherForecast.setTimestamp(dayForecast.getLong("dt"));

                // temperature
                JSONObject tempForecast = dayForecast.getJSONObject("temp");
                weatherForecast.setDay((int) tempForecast.getDouble("day"));
                weatherForecast.setMorning((int) tempForecast.getDouble("morn"));
                weatherForecast.setEve((int) tempForecast.getDouble("eve"));
                weatherForecast.setNight((int) tempForecast.getDouble("night"));
                weatherForecast.setMin((int) tempForecast.getDouble("min"));
                weatherForecast.setMax((int) tempForecast.getDouble("max"));

                // Weather
                JSONArray weatherForecastJSON = dayForecast.getJSONArray("weather");
                JSONObject jsonObjectWeather = weatherForecastJSON.getJSONObject(0);

                weatherForecast.setCity(city);
                weatherForecast.setDescription(jsonObjectWeather.getString("description"));
                weatherForecast.setIcon(jsonObjectWeather.getString("icon"));
                mWeathers.add(weatherForecast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Weather> getDaysForecasts() {
        return mWeathers;
    }
}
