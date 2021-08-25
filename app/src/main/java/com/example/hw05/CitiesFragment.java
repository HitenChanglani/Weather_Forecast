package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CitiesFragment extends Fragment implements CityRecyclerViewAdapter.AdapterInterface {

    RecyclerView cityListRecyclerView;
    LinearLayoutManager layoutManager;
    CityRecyclerViewAdapter adapter;
    CityInterface mListener;

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityInterface){
            mListener = (CityInterface) context;
        }
        else{
            throw new RuntimeException(getContext().toString() + " must be implemented");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        getActivity().setTitle(getResources().getString(R.string.cityTitle));

        cityListRecyclerView = view.findViewById(R.id.cityListRecyclerView);
        cityListRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        cityListRecyclerView.setLayoutManager(layoutManager);
        adapter = new CityRecyclerViewAdapter(Data.cities, this);
        cityListRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void passDataToCityFragment(Data.City city) {
        mListener.goToCurrentWeatherFragment(city);
    }

    interface CityInterface{
        void goToCurrentWeatherFragment(Data.City city);
    }

}