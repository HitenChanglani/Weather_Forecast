package com.example.hw05;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

    ArrayList<TotalTemperatureData> temperatureData;

    public ForecastRecyclerViewAdapter(ArrayList<TotalTemperatureData> data) {
        this.temperatureData = data;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_layout, parent, false);
        ForecastViewHolder viewHolder = new ForecastViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        TotalTemperatureData data = temperatureData.get(position);
        String icon = data.getWeather().get(0).getIcon();
        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

        holder.dateLayout.setText(data.getDateInfo());
        holder.currentTempLayout.setText(data.getMainTemp().getTemp() + " F");
        holder.maxTempLayout.setText("Max: " + data.getMainTemp().getTemp_max() + " F");
        holder.minTempLayout.setText("Min: " + data.getMainTemp().getTemp_min() + " F");
        holder.humidityValueLayout.setText(data.getMainTemp().getHumidity() + "%");
        holder.descriptionLayout.setText(data.getWeather().get(0).getDescription());

        Picasso.get()
                .load(iconUrl)
                .into(holder.weatherIconForecast);

    }

    @Override
    public int getItemCount() {
        return temperatureData.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder{
        TextView dateLayout, currentTempLayout, maxTempLayout, minTempLayout, humidityValueLayout, descriptionLayout;
        ImageView weatherIconForecast;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateLayout = itemView.findViewById(R.id.dateLayout);
            currentTempLayout = itemView.findViewById(R.id.currentTempLayout);
            maxTempLayout = itemView.findViewById(R.id.maxTempLayout);
            minTempLayout = itemView.findViewById(R.id.minTempLayout);
            humidityValueLayout = itemView.findViewById(R.id.humidityValueLayout);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            weatherIconForecast = itemView.findViewById(R.id.weatherIconForecast);
        }
    }

}
