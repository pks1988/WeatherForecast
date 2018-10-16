package com.weather.forecast;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourWeather;
import com.weather.forecast.fragments.MapFragment;
import com.weather.forecast.fragments.WeatherDetailFragment;
import com.weather.forecast.fragments.WeatherFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForeCastActivity extends FragmentActivity implements MapFragment.OnMapFragmentListener,
        WeatherFragment.OnWeatherFragmentListener, PermissionListener, WeatherDetailFragment.OnWeatherDetailListener {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_cast);
        ButterKnife.bind(this);
        askForPermission();
    }

    private void askForPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check();
    }

    private void replaceFragment(Fragment mFragment, String backStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment, backStack);
        ft.addToBackStack(backStack);
        ft.commit();
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        MapFragment mTaskFragment = (MapFragment) fm.findFragmentByTag(MapFragment.class.getSimpleName());
        if (mTaskFragment == null)
            replaceFragment(MapFragment.newInstance(), MapFragment.class.getSimpleName());
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        askForPermission();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }

    @Override
    public void onMapFragmentInteraction(LatLng mLatLng, String locality) {
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        WeatherFragment mTaskFragment = (WeatherFragment) fm.findFragmentByTag(WeatherFragment.class.getSimpleName());
        if (mTaskFragment == null)
            replaceFragment(WeatherFragment.newInstance(mLatLng.latitude, mLatLng.longitude, locality), WeatherFragment.class.getSimpleName());
    }

    @Override
    public void onWeatherFragmentInteraction(ThreeHourWeather hourWeather) {
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        WeatherDetailFragment mTaskFragment = (WeatherDetailFragment) fm.findFragmentByTag(WeatherDetailFragment.class.getSimpleName());
        if (mTaskFragment == null)
            replaceFragment(WeatherDetailFragment.newInstance(new Gson().toJson(hourWeather)), WeatherDetailFragment.class.getSimpleName());
    }

    @Override
    public void onDetailsFragmentInteraction(Uri uri) {}

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStackImmediate();

    }

}
