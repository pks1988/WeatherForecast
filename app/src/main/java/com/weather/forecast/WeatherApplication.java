package com.weather.forecast;

import android.app.Application;

import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherApplication extends Application {
    private static WeatherApplication mApplicationInstance;
    OpenWeatherMapHelper helper;

    private WeatherApplication() {
    }

    public static WeatherApplication getApplicationInstance() {
        if (mApplicationInstance == null) {
            mApplicationInstance = new WeatherApplication();
        }
        return mApplicationInstance;
    }

    public OpenWeatherMapHelper initOpenWeatherHelper() {
        helper = new OpenWeatherMapHelper();
        helper.setApiKey("c6e381d8c7ff98f0fee43775817cf6ad");
        helper.setUnits(Units.METRIC);
        return helper;
    }
}

