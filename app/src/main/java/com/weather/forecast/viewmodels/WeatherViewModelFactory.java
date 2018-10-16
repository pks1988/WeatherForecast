package com.weather.forecast.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    Application application;
    double latitude;
    double longitude;
    String locality;

    public WeatherViewModelFactory(Application application, double latitude, double longitude, String locality) {
        this.application = application;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locality = locality;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(application, latitude, longitude,locality);
    }
}
