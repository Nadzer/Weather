package com.weather.listeners;

import com.weather.core.Weather;

import java.util.ArrayList;

/**
 * Created by Nadzer on 23/05/2016.
 */
public interface ResultListener {
    void onSuccessfulReturn(ArrayList<Weather> weather);
}
