package com.weather.forecast.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourWeather;
import com.weather.forecast.R;
import com.weather.forecast.adapters.WeatherAdapter;
import com.weather.forecast.utility.Utility;
import com.weather.forecast.viewmodels.WeatherViewModel;
import com.weather.forecast.viewmodels.WeatherViewModelFactory;
import com.weather.forecast.widgets.RecyclerTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeatherFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_LAT = "latitude";
    private static final String ARG_LNG = "longitude";
    private static final String ARG_LOCALITY = "locality";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.recyclerview_forecast)
    RecyclerView recyclerviewForecast;
    Unbinder unbinder;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private double latitude;
    private double longitude;
    private String locality;
    WeatherViewModel weatherViewModel;

    private OnWeatherFragmentListener mListener;
    private WeatherAdapter adapter;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(double latitude, double longitude, String locality) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, latitude);
        args.putDouble(ARG_LNG, longitude);
        args.putString(ARG_LOCALITY, locality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LAT);
            longitude = getArguments().getDouble(ARG_LAT);
            locality = getArguments().getString(ARG_LOCALITY);
        }
        if (getActivity() == null) return;
        weatherViewModel = ViewModelProviders.of(this, new WeatherViewModelFactory(getActivity().getApplication(), latitude, longitude, locality)).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        unbinder = ButterKnife.bind(this, view);
        weatherConfigs();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void weatherConfigs() {
        adapterSetup();
        registerUserObserver();
        setRecyclerClicks();
    }

    private void adapterSetup() {
        swipeRefresh.setEnabled(true);
        swipeRefresh.setRefreshing(true);
        adapter = new WeatherAdapter(getActivity());
        recyclerviewForecast.setAdapter(adapter);
        recyclerviewForecast.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void registerUserObserver() {
        if (weatherViewModel == null) return;
        weatherViewModel.getFutureWeatherForecast().observe(this, new Observer<ThreeHourForecast>() {
            @Override
            public void onChanged(@Nullable ThreeHourForecast threeHourForecast) {
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);

                if (threeHourForecast == null) {
                    Utility.showToast(getActivity(), "Unable to find weather report for your city");
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                    return;
                }

                if (threeHourForecast == null) return;
                collapsingToolbar.setTitle(threeHourForecast.getCity().getName());
                updateCurrentWeather(threeHourForecast);
                adapter.updateWeather(threeHourForecast.getThreeHourWeatherArray());
            }
        });
    }

    void setRecyclerClicks() {
        recyclerviewForecast.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerviewForecast, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                showWeatherDetails(position);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void showWeatherDetails(int pos) {
        onRecycleItemClick(adapter.getSelectedWeather(pos));
    }

    private void updateCurrentWeather(ThreeHourForecast threeHourForecast) {
        Utility.setHighTemperature(highTemperature, 0, threeHourForecast.getThreeHourWeatherArray(), getActivity(), null);
        Utility.setLowTemperature(lowTemperature, 0, threeHourForecast.getThreeHourWeatherArray(), getActivity(), null);
        Utility.setImageResourceForWeather(weatherIcon, 0, threeHourForecast.getThreeHourWeatherArray(), getActivity(), null);
        Utility.setReadableDateFormat(date, 0, threeHourForecast.getThreeHourWeatherArray(), getActivity(), null);
        Utility.setWeatherDescription(weatherDescription, 0, threeHourForecast.getThreeHourWeatherArray(), getActivity(), null);
    }

    public void onRecycleItemClick(ThreeHourWeather hourWeather) {
        if (mListener != null) {
            mListener.onWeatherFragmentInteraction(hourWeather);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnWeatherFragmentListener {
        void onWeatherFragmentInteraction(ThreeHourWeather hourWeather);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWeatherFragmentListener) {
            mListener = (OnWeatherFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
