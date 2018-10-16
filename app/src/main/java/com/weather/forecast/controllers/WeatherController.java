package com.weather.forecast.controllers;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.weather.forecast.WeatherApplication;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherController {
    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<CurrentWeather> mCurrentWeatherLiveData = new MutableLiveData<>();
    private MutableLiveData<ThreeHourForecast> mFutureWeatherLiveData = new MutableLiveData<>();


    /*public LiveData<List<CurrentWeather>> getCurrentWeatherReport(double latitude, double longitude) {
        WeatherApplication.getApplicationInstance().initOpenWeatherHelper().getCurrentWeatherByGeoCoordinates(latitude, longitude, new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Log.v(TAG,
                        "Coordinates: " + currentWeather.getCoord().getLat() + ", " + currentWeather.getCoord().getLon() + "\n"
                                + "Weather Description: " + currentWeather.getWeatherArray().get(0).getDescription() + "\n"
                                + "Max Temperature: " + currentWeather.getMain().getTempMax() + "\n"
                                + "Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                + "City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v(TAG, throwable.getMessage());
            }
        });
    }*/

    public LiveData<ThreeHourForecast> getFutureForecastReport(double latitude, double longitude,String localityName) {
        WeatherApplication.getApplicationInstance().initOpenWeatherHelper().getThreeHourForecastByCityName(localityName, new OpenWeatherMapHelper.ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                mFutureWeatherLiveData.postValue(threeHourForecast);
                Log.e("data", new Gson().toJson(threeHourForecast));
                Log.v(TAG,
                        "City/Country: " + threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() + "\n"
                                + "Forecast Array Count: " + threeHourForecast.getCnt() + "\n"
                                //For this example, we are logging details of only the first forecast object in the forecasts array
                                + "First Forecast Date Timestamp: " + threeHourForecast.getThreeHourWeatherArray().get(0).getDt() + "\n"
                                + "First Forecast Weather Description: " + threeHourForecast.getThreeHourWeatherArray().get(0).getWeatherArray().get(0).getDescription() + "\n"
                                + "First Forecast Max Temperature: " + threeHourForecast.getThreeHourWeatherArray().get(0).getMain().getTempMax() + "\n"
                                + "First Forecast Wind Speed: " + threeHourForecast.getThreeHourWeatherArray().get(0).getWind().getSpeed() + "\n"
                );
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v(TAG, throwable.getMessage());
            }
        });

        return mFutureWeatherLiveData;
    }


}

