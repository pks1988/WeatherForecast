package com.weather.forecast.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourWeather;
import com.weather.forecast.R;
import com.weather.forecast.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeatherDetailFragment extends Fragment {
    private static final String ARG_WEATHER = "param1";
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.weather_description)
    TextView weatherDescription;
    @BindView(R.id.high_temperature)
    TextView highTemperature;
    @BindView(R.id.low_temperature)
    TextView lowTemperature;
    @BindView(R.id.horizontal_middle)
    Guideline horizontalMiddle;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.wind_measurement)
    TextView windMeasurement;
    Unbinder unbinder;

    private String mWeather;
    private String mParam2;
    private OnWeatherDetailListener mListener;
    private ThreeHourWeather threeHourForecast;

    public WeatherDetailFragment() {
    }

    public static WeatherDetailFragment newInstance(String mHourWeather) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WEATHER, mHourWeather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mWeather = getArguments().getString(ARG_WEATHER);
            threeHourForecast = new Gson().fromJson(mWeather, ThreeHourWeather.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setRetainInstance(true);
        updateUI();
        return view;
    }

    private void updateUI() {
        Utility.setHighTemperature(highTemperature, 0, null, getActivity(), threeHourForecast);
        Utility.setLowTemperature(lowTemperature, 0, null, getActivity(), threeHourForecast);
        Utility.setImageResourceForWeather(weatherIcon, 0, null, getActivity(), threeHourForecast);
        Utility.setReadableDateFormat(date, 0, null, getActivity(), threeHourForecast);
        Utility.setWeatherDescription(weatherDescription, 0, null, getActivity(), threeHourForecast);

        setHumidity();
        setPressure();
        setWind();

    }

    private void setWind() {
        double windSpeed = threeHourForecast.getWind().getSpeed();
        double windDirection = threeHourForecast.getWind().getDeg();
        String windString = Utility.getFormattedWind(getActivity(), windSpeed, windDirection);
        String windA11y = getString(R.string.a11y_wind, windString);
        windMeasurement.setText(windString);
        windMeasurement.setContentDescription(windA11y);
    }

    private void setPressure() {
        double pressure = threeHourForecast.getMain().getPressure();
        String pressureString = getString(R.string.format_pressure, pressure);
        String pressureA11y = getString(R.string.a11y_pressure, pressureString);
        this.pressure.setText(pressureString);
        this.pressure.setContentDescription(pressureA11y);
    }

    private void setHumidity() {
        double humidity = threeHourForecast.getMain().getHumidity();
        String humidityString = getString(R.string.format_humidity, humidity);
        String humidityA11y = getString(R.string.a11y_humidity, humidityString);
        this.humidity.setText(humidityString);
        this.humidity.setContentDescription(humidityA11y);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnWeatherDetailListener {
        void onDetailsFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWeatherDetailListener) {
            mListener = (OnWeatherDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
