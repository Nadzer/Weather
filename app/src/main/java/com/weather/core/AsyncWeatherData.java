package com.weather.core;

import android.os.AsyncTask;

import com.weather.listeners.ResultListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nadzer
 * 23/05/2016.
 */
public class AsyncWeatherData extends AsyncTask<String, String, ArrayList<Weather>> {
    private WeatherParser mWeatherParser;
    public ResultListener listener;

    public void setOnResultListener(ResultListener listener) {
        this.listener = listener;
    }

    public void getData(AsyncWeatherData data) {
        listener = data.listener;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();

        try {
            URL url;
            if (params.length >= 1) {
                url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast/" +
                                "daily?q=%s&mode=json&units=metric&cnt=7" +
                                "&appid=4730a8758d1f1ddbe29601e07bc308c2",
                        params[0]));
            } else {
                url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Paris" +
                        "&mode=json&units=metric&cnt=7&appid=4730a8758d1f1ddbe29601e07bc308c2");
            }

            // Create the request to the Weather API and open a connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            mWeatherParser = new WeatherParser(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mWeatherParser.getDaysForecasts();
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        listener.onSuccessfulReturn(weathers);
        super.onPostExecute(weathers);
    }
}
