package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.textclassifier.ConversationActions;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CityInterface, CurrentWeatherFragment.CurrentWeatherInterface {

    public static final String API_KEY = "9d0bf36745a0e6b696ae8d6fa7323b1e";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new CitiesFragment())
                .commit();

    }

    @Override
    public void goToCurrentWeatherFragment(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, CurrentWeatherFragment.newInstance(city, API_KEY))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToWeatherForeCast(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, WeatherForecastFragment.newInstance(city, API_KEY))
                .addToBackStack(null)
                .commit();
    }
}