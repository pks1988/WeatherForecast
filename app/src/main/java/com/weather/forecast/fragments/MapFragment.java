package com.weather.forecast.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.weather.forecast.R;
import com.weather.forecast.utility.Utility;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import locationprovider.davidserrano.com.LocationProvider;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
        View.OnClickListener, PlaceSelectionListener {


    private final String TAG = this.getClass().getSimpleName();
    Unbinder unbinder;
    SupportMapFragment mapFragment;
    @BindView(R.id.txtLocationAddress)
    AppCompatTextView txtLocationAddress;
    @BindView(R.id.btnCheckWeather)
    AppCompatButton btnCheckWeather;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private List<Address> addressList;
    private OnMapFragmentListener mListener;
    private LatLng mLatLng;
    private float mapLat,mapLng;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        LocationProvider locationProvider = new LocationProvider.Builder().setContext(getActivity()).setListener(callback).create();
        locationProvider.requestLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mapFragment.getMapAsync(this);
        btnCheckWeather.setOnClickListener(this);

    }

    public void checkWeather() {
        if (mListener != null) {
            String locality = Utility.getLocalityName(addressList, getActivity());
            if (locality != null) {
                mListener.onMapFragmentInteraction(mLatLng, locality);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapFragmentListener) {
            mListener = (OnMapFragmentListener) context;
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

    @Override
    public void onCameraIdle() {
        mLatLng = mMap.getCameraPosition().target;
        geocoder = new Geocoder(getActivity());
        setPlaceAutoComplete();
        try {
            addressList = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
            Utility.getAddressFromLatLng(addressList, getActivity(), txtLocationAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(mapLat, mapLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.setBuildingsEnabled(true);
        mMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckWeather:
                if (mLatLng != null)
                    checkWeather();
                else
                    Utility.showToast(getActivity(), "Unable to get the position");
                break;
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
        mLatLng = place.getLatLng();
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    public interface OnMapFragmentListener {
        void onMapFragmentInteraction(LatLng mLatLng, String locality);
    }

    private void setPlaceAutoComplete() {
        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {
        @Override
        public void onNewLocationAvailable(float lat, float lon) {
            //location update
            mapLat=lat;
            mapLng=lon;
        }

        @Override
        public void locationServicesNotEnabled() {
            //failed finding a location
        }

        @Override
        public void updateLocationInBackground(float lat, float lon) {
            //if a listener returns after the main locationAvailable callback, it will go here
        }

        @Override
        public void networkListenerInitialised() {
            //when the library switched from GPS only to GPS & network
        }
    };

}
