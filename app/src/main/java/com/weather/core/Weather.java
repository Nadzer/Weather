package com.weather.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nadzer on 23/05/2016.
 */
public class Weather implements Parcelable {
    private SimpleDateFormat date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private int day;
    private int morning;
    private int eve;
    private int night;
    private int min;
    private int max;
    private long timestamp;
    private String description;
    private String condition;
    private String icon;

    private String city;

    public Weather() {
    }

    protected Weather(Parcel in) {
        day = in.readInt();
        morning = in.readInt();
        eve = in.readInt();
        night = in.readInt();
        min = in.readInt();
        max = in.readInt();
        timestamp = in.readLong();
        description = in.readString();
        condition = in.readString();
        icon = in.readString();
        city = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    // Getters
    public SimpleDateFormat getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public int getDay() {
        return day;
    }

    public int getMorning() {
        return morning;
    }

    public int getEve() {
        return eve;
    }

    public int getNight() {
        return night;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getDateToString() {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        Long timeStamp = timestamp * 1000;

        Date realDate = (new Date(timeStamp));

        return sdf.format(realDate);
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    public String getIcon() {
        return icon;
    }

    // Setters
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public void setEve(int eve) {
        this.eve = eve;
    }

    public void setMorning(int morning) {
        this.morning = morning;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(morning);
        dest.writeInt(eve);
        dest.writeInt(night);
        dest.writeInt(min);
        dest.writeInt(max);
        dest.writeLong(timestamp);
        dest.writeString(description);
        dest.writeString(condition);
        dest.writeString(icon);
        dest.writeString(city);
    }
}
