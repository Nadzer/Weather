package com.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.R;
import com.weather.core.Weather;

import java.util.ArrayList;

/**
 * Created by Nadzer
 * 23/05/2016.
 */
public class WeatherListAdapter extends BaseAdapter {
    private ArrayList<Weather> weatherArrayList;
    private Context mContext;

    public WeatherListAdapter(ArrayList<Weather> weatherArrayList, Context mContext) {
        this.weatherArrayList = weatherArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return weatherArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_list, parent, false);

        ImageView image = (ImageView) view.findViewById(R.id.image_weather_list);
        TextView temp = (TextView) view.findViewById(R.id.temperature_list);
        TextView date = (TextView) view.findViewById(R.id.date_list);

        Weather weather = weatherArrayList.get(position);
        image.setImageResource(getWeatherImages(weather.getDescription()));
        temp.setText(String.format("%s°C", String.valueOf(weather.getDay())));
        date.setText(weather.getDateToString());

        return view;
    }

    private int getWeatherImages(String data) {
        switch (data) {
            case "clear sky":
                return R.drawable.w01d;
            case "few clouds":
                return R.drawable.w02d;
            case "scattered clouds":
                return R.drawable.w03d;
            case "broken clouds":
                return R.drawable.w04d;
            case "moderate rain":
                return R.drawable.w09d;
            case "rain":
                return R.drawable.w10d;
            case "thunderstorm":
                return R.drawable.w11d;
            case "snow":
                return R.drawable.w13d;
            case "mist":
                return R.drawable.w50d;
            default:
                return R.drawable.w03d;
        }
    }
}
