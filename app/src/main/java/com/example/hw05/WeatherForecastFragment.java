package com.example.hw05;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeatherForecastFragment extends Fragment {

    Data.City city;
    String apiID;
    RecyclerView weatherForecastRecyclerView;
    LinearLayoutManager layoutManager;
    TextView cityWeatherForecastTextView;
    ForecastRecyclerViewAdapter adapter;

    private static final String ARG_CITY = "ARG_CITY";
    private static final String ARG_API_ID = "ARG_API_ID";


    public WeatherForecastFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WeatherForecastFragment newInstance(Data.City city, String id) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        getActivity().setTitle(getResources().getString(R.string.weatherForecastTitle));

        cityWeatherForecastTextView = view.findViewById(R.id.cityWeatherForecastTextView);
        weatherForecastRecyclerView = view.findViewById(R.id.weatherForecastRecyclerView);

        weatherForecastRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        weatherForecastRecyclerView.setLayoutManager(layoutManager);

        new GetWeatherForecastData().execute(city.getCity(), city.getCountry(), apiID);

        return view;
    }


    class GetWeatherForecastData extends AsyncTask<String, Integer, ArrayList<TotalTemperatureData>>{

        final OkHttpClient client = new OkHttpClient();
        String cityName, countryName;

        @Override
        protected ArrayList<TotalTemperatureData> doInBackground(String... strings) {

            cityName = strings[0];
            countryName = strings[1];

            HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast").newBuilder()
                    .addQueryParameter("q", strings[0])
                    .addQueryParameter("appid", strings[2])
                    .addQueryParameter("units", "imperial")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            ArrayList<TotalTemperatureData> informationList = new ArrayList<>();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray listArray = object.getJSONArray("list");

                    for (int i = 0; i < listArray.length(); i++){

                        TotalTemperatureData data = new TotalTemperatureData();
                        WeatherDescription weather = new WeatherDescription();
                        ArrayList<WeatherDescription> weatherList = new ArrayList<>();
                        Wind wind = new Wind();
                        MainTemperatureInformation temperatureInformation = new MainTemperatureInformation();
                        Cloud cloud = new Cloud();

                        JSONObject jsonObject = listArray.getJSONObject(i);
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
                        data.setDateInfo(jsonObject.getString("dt_txt"));

                        informationList.add(i, data);

                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return informationList;
        }


        @Override
        protected void onPostExecute(ArrayList<TotalTemperatureData> totalTemperatureData) {

            if (totalTemperatureData.size() != 0){
                cityWeatherForecastTextView.setText(cityName + ", " + countryName);
                adapter = new ForecastRecyclerViewAdapter(totalTemperatureData);
                weatherForecastRecyclerView.setAdapter(adapter);
            }
            else {
                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }

}