package com.weather.forecast.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourWeather;
import com.weather.forecast.R;
import com.weather.forecast.utility.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pravesh Sharma on 16-10-2018.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private Context context;
    LayoutInflater mInflater;
    private List<ThreeHourWeather> forecastList;

    public WeatherAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.forecast_list_item, viewGroup, false);
        return new WeatherViewHolder(itemView);
    }

    public ThreeHourWeather getSelectedWeather(int pos) {
        return forecastList.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int pos) {
        Utility.setImageResourceForWeather(holder.weatherIcon, pos, forecastList, context, null);
        Utility.setReadableDateFormat(holder.date, pos, forecastList, context, null);
        Utility.setHighTemperature(holder.highTemperature, pos, forecastList, context, null);
        Utility.setLowTemperature(holder.lowTemperature, pos, forecastList, context, null);
        Utility.setWeatherDescription(holder.weatherDescription, pos, forecastList, context, null);
    }

    public void updateWeather(List<ThreeHourWeather> forecast) {
        this.forecastList = forecast;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (forecastList != null)
            return forecastList.size();
        else return 0;
    }


    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weather_icon)
        ImageView weatherIcon;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.weather_description)
        TextView weatherDescription;
        @BindView(R.id.high_temperature)
        TextView highTemperature;
        @BindView(R.id.low_temperature)
        TextView lowTemperature;
        @BindView(R.id.guideline)
        Guideline guideline;

        WeatherViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
