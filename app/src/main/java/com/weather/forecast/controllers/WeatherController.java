package com.weather.forecast.controllers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.weather.forecast.WeatherApplication;
import com.weather.forecast.utility.Utility;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherController {
    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<ThreeHourForecast> mFutureWeatherLiveData = new MutableLiveData<>();

    public LiveData<ThreeHourForecast> getFutureForecastReport(double latitude, double longitude, String localityName) {
        WeatherApplication.getApplicationInstance().initOpenWeatherHelper().getThreeHourForecastByCityName(localityName, new OpenWeatherMapHelper.ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                mFutureWeatherLiveData.postValue(threeHourForecast);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v(TAG, throwable.getMessage());
                mFutureWeatherLiveData.postValue(null);
            }
        });

        return mFutureWeatherLiveData;
    }


}

