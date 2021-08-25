package com.example.hw05;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrentWeatherFragment extends Fragment {

    private static final String ARG_CITY = "ARG_CITY";
    private static final String ARG_API_ID = "ARG_API_ID";

    Data.City city;
    String apiID;
    CurrentWeatherInterface mListener;
    TextView cityNameTextView, tempValueTextView, tempMaxValueTextView, tempMinValueTextView, descValueTextView, humidityValueTextView, windspeedValueTextView, windDegreeValueTextView, cloudinessValueTextView;
    ImageView weatherIcon;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CurrentWeatherFragment newInstance(Data.City item, String id) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, item);
        args.putString(ARG_API_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (Data.City) getArguments().getSerializable(ARG_CITY);
            apiID = getArguments().getString(ARG_API_ID);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CurrentWeatherInterface){
            mListener = (CurrentWeatherInterface) context;
        }
        else {
            throw new RuntimeException(getContext().toString() + " must be implemented");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        getActivity().setTitle(getResources().getString(R.string.currentWeatherTitle));

        cityNameTextView = view.findViewById(R.id.cityNameTextView);
        tempValueTextView = view.findViewById(R.id.tempValueTextView);
        tempMaxValueTextView = view.findViewById(R.id.tempMaxValueTextView);
        tempMinValueTextView = view.findViewById(R.id.tempMinValueTextView);
        descValueTextView = view.findViewById(R.id.descValueTextView);
        humidityValueTextView = view.findViewById(R.id.humidityValueTextView);
        windspeedValueTextView = view.findViewById(R.id.windspeedValueTextView);
        windDegreeValueTextView = view.findViewById(R.id.windDegreeValueTextView);
        cloudinessValueTextView = view.findViewById(R.id.cloudinessValueTextView);
        weatherIcon = view.findViewById(R.id.weatherIconCurrentWeather);

        view.findViewById(R.id.checkForecastButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToWeatherForeCast(city);
            }
        });

        new GetCurrentWeatherData().execute(city.getCity(), city.getCountry(), apiID);

        return view;
    }


    class GetCurrentWeatherData extends AsyncTask<String, Integer, TotalTemperatureData>{

        final OkHttpClient client = new OkHttpClient();
        String cityName, countryName;


        @Override
        protected TotalTemperatureData doInBackground(String... strings) {

            cityName = strings[0];
            countryName = strings[1];

            HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder()
                    .addQueryParameter("q", strings[0])
                    .addQueryParameter("appid", strings[2])
                    .addQueryParameter("units", "imperial")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            TotalTemperatureData data = new TotalTemperatureData();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    WeatherDescription weather = new WeatherDescription();
                    ArrayList<WeatherDescription> weatherList = new ArrayList<>();
                    Wind wind = new Wind();
                    MainTemperatureInformation temperatureInformation = new MainTemperatureInformation();
                    Cloud cloud = new Cloud();

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherJSONObject = weatherArray.getJSONObject(0);
                    JSONObject mainTempJSONObject = jsonObject.getJSONObject("main");
                    JSONObject windJSONObject = jsonObject.getJSONObject("wind");
                    JSONObject cloudJSONObject = jsonObject.getJSONObject("clouds");

                    weather.setDescription(weatherJSONObject.getString("description"));
                    weather.setIcon(weatherJSONObject.getString("icon"));
                    weatherList.add(weather);

                    temperatureInformation.setTemp(mainTempJSONObject.getString("temp"));
                    temperatureInformation.setTemp_max(mainTempJSONObject.getString("temp_max"));
                    temperatureInformation.setTemp_min(mainTempJSONObject.getString("temp_min"));
                    temperatureInformation.setHumidity(mainTempJSONObject.getString("humidity"));
                    temperatureInformation.setPressure(mainTempJSONObject.getString("pressure"));

                    wind.setDeg(windJSONObject.getString("deg"));
                    wind.setSpeed(windJSONObject.getString("speed"));

                    cloud.setCloudiness(cloudJSONObject.getString("all"));

                    data.setMainTemp(temperatureInformation);
                    data.setWeather(weatherList);
                    data.setWind(wind);
                    data.setCloud(cloud);

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return data;
        }


        @Override
        protected void onPostExecute(TotalTemperatureData totalTemperatureData) {

            if (totalTemperatureData != null){

                String icon = totalTemperatureData.getWeather().get(0).getIcon();
                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                cityNameTextView.setText(cityName + ", " + countryName);
                tempValueTextView.setText(totalTemperatureData.mainTemp.getTemp() + " F");
                tempMaxValueTextView.setText(totalTemperatureData.mainTemp.getTemp_max() + " F");
                tempMinValueTextView.setText(totalTemperatureData.mainTemp.getTemp_min() + " F");
                descValueTextView.setText(totalTemperatureData.weather.get(0).description);
                humidityValueTextView.setText(totalTemperatureData.mainTemp.getHumidity() + "%");
                windspeedValueTextView.setText(totalTemperatureData.wind.getSpeed() + " miles/hr");
                windDegreeValueTextView.setText(totalTemperatureData.wind.getDeg() + " degrees");
                cloudinessValueTextView.setText(totalTemperatureData.cloud.getCloudiness() + " %");

                Picasso.get()
                        .load(iconUrl)
                        .into(weatherIcon);

            }
            else {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }


    interface CurrentWeatherInterface{
        void goToWeatherForeCast(Data.City city);
    }
}