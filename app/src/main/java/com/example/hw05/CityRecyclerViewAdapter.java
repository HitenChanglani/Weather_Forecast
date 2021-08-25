package com.example.hw05;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.CityViewHolder> {

    ArrayList<Data.City> cityList;
    AdapterInterface mListener;

    public CityRecyclerViewAdapter(ArrayList<Data.City> cities, AdapterInterface adapterInterface){
        this.cityList = cities;
        this.mListener = adapterInterface;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        CityViewHolder viewHolder = new CityViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        Data.City city = cityList.get(position);
        holder.text1.setText(city.getCity() + ", " + city.getCountry());
        holder.position = position;
        holder.city = city;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder{
        TextView text1;
        int position;
        Data.City city;
        AdapterInterface mListener;

        public CityViewHolder(@NonNull View itemView, AdapterInterface adapterInterface) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            this.mListener = adapterInterface;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.passDataToCityFragment(city);
                }
            });
        }
    }

    interface AdapterInterface{
        void passDataToCityFragment(Data.City city);
    }
}
