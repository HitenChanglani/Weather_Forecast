package com.example.hw05;

import java.util.ArrayList;

public class TotalTemperatureData {
    ArrayList<WeatherDescription> weather;
    MainTemperatureInformation mainTemp;
    Wind wind;
    Cloud cloud;
    String dateInfo;

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public ArrayList<WeatherDescription> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherDescription> weather) {
        this.weather = weather;
    }

    public MainTemperatureInformation getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(MainTemperatureInformation mainTemp) {
        this.mainTemp = mainTemp;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    @Override
    public String toString() {
        return "TotalTemperatureData{" +
                "weather=" + weather +
                ", mainTemp=" + mainTemp +
                ", wind=" + wind +
                ", cloud=" + cloud +
                ", dateInfo='" + dateInfo + '\'' +
                '}';
    }
}
