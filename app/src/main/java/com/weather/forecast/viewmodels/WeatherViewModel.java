package com.weather.forecast.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.weather.forecast.controllers.WeatherController;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<ThreeHourForecast> mHourForecastLiveData;
    private WeatherController mWeatherController;

    public WeatherViewModel(@NonNull Application application, double latitude, double longitude, String locality) {
        super(application);
        mWeatherController = new WeatherController();
        mHourForecastLiveData = mWeatherController.getFutureForecastReport(latitude, longitude, locality);
    }

    public LiveData<ThreeHourForecast> getFutureWeatherForecast() {
        return mHourForecastLiveData;
    }
}
