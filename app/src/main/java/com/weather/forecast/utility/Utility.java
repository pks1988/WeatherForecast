package com.weather.forecast.utility;

import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourWeather;
import com.weather.forecast.R;

import java.util.List;

/**
 * Created by Pravesh Sharma on 15-10-2018.
 */

public class Utility {

    private final static String TAG = Utility.class.getSimpleName();

    public static void getAddressFromLatLng(List<Address> mAddressList, Context mContext, TextView mTextView) {
        StringBuilder strAddress;
        try {
            if (mAddressList != null && !mAddressList.isEmpty()) {
                Address address = mAddressList.get(0);
                String locality = mAddressList.get(0).getAddressLine(0);
                String country = mAddressList.get(0).getCountryName();
                String city = mAddressList.get(0).getLocality();
                strAddress = new StringBuilder();
                if (!locality.isEmpty() && !country.isEmpty()) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        strAddress.append(address.getAddressLine(i)).append(" ");
                    }
                }
                Log.v("address", city);
                mTextView.setText(String.format("%s  %s", locality, country));
            }

        } catch (Exception e) {
            showToast(mContext, "Could not get address..!");
            Log.getStackTraceString(e);
        }

    }

    public static String getLocalityName(List<Address> mAddressList, Context mContext) {
        try {
            String locality = mAddressList.get(0).getLocality();
            if (locality != null) {
                return locality;
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
            showToast(mContext, "Unable to fetch city name..please try again!");
        }

        return null;
    }

    public static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    public static int getResourceIdForWeatherCondition(Long weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.light_rainy;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.heavy_rainy;
        } else if (weatherId == 511) {
            return R.drawable.snowy;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.heavy_rainy;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.snowy;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.foogy;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.storm;
        } else if (weatherId == 800) {
            return R.drawable.clear;
        } else if (weatherId == 801) {
            return R.drawable.light_cloudy;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.cloudy;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.storm;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.storm;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.clear;
        }

        Log.e(TAG, "Unknown Weather: " + weatherId);
        return R.drawable.storm;
    }


    public static String getFormattedWind(Context context, double windSpeed, double degrees) {
        int windFormat = R.string.format_wind_kmh;

        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }

        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    public static String formatTemperature(Context context, double temperature) {
        int temperatureFormatResourceId = R.string.format_temperature;
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }


    public static void setReadableDateFormat(TextView dateView, int pos, List<ThreeHourWeather> forecastList, Context context, ThreeHourWeather threeHourForecast) {
        //long dateInMillis = forecastList.get(pos).getWeatherArray().get(0).getId();
        //String dateString = com.weather.forecast.utility.DateUtils.getFriendlyDateString(context, dateInMillis, false);
        if (forecastList != null)
            dateView.setText(forecastList.get(pos).getDtTxt());
        else
            dateView.setText(threeHourForecast.getDtTxt());
    }

    public static void setImageResourceForWeather(ImageView weatherIcon, int pos, List<ThreeHourWeather> forecastList, Context context, ThreeHourWeather threeHourForecast) {
        if (forecastList != null)
            weatherIcon.setImageResource(Utility.getResourceIdForWeatherCondition(forecastList.get(pos).getWeatherArray().get(0).getId()));
        else
            weatherIcon.setImageResource(Utility.getResourceIdForWeatherCondition(threeHourForecast.getWeatherArray().get(0).getId()));
    }

    public static void setHighTemperature(TextView highTemperature, int pos, List<ThreeHourWeather> forecastList, Context context, ThreeHourWeather threeHourForecast) {
        String highString;
        if (forecastList != null)
            highString = Utility.formatTemperature(context, forecastList.get(pos).getMain().getTempMax());
        else
            highString = Utility.formatTemperature(context, threeHourForecast.getMain().getTempMax());

        String highA11y = context.getString(R.string.a11y_high_temp, highString);
        highTemperature.setText(highString);
        highTemperature.setContentDescription(highA11y);
    }


    public static void setLowTemperature(TextView lowTemperature, int pos, List<ThreeHourWeather> forecastList, Context context, ThreeHourWeather threeHourForecast) {
        double lowInCelsius;
        if (forecastList != null)
            lowInCelsius = forecastList.get(pos).getMain().getTempMin();
        else
            lowInCelsius = threeHourForecast.getMain().getTempMin();


        String lowString = Utility.formatTemperature(context, lowInCelsius);
        String lowA11y = context.getString(R.string.a11y_low_temp, lowString);
        lowTemperature.setText(lowString);
        lowTemperature.setContentDescription(lowA11y);
    }

    public static void setWeatherDescription(TextView weatherDescription, int pos, List<ThreeHourWeather> forecastList, Context context, ThreeHourWeather threeHourForecast) {
        if (forecastList != null)
            weatherDescription.setText(forecastList.get(pos).getWeatherArray().get(0).getDescription());
        else
            weatherDescription.setText(threeHourForecast.getWeatherArray().get(0).getDescription());

    }
}
