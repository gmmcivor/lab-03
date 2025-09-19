package com.example.listycitylab3;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.CityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    public void addCity(City city) {
        dataList.add(city);
        cityAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Added " + city.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editCity(int position, City city) {
        if (position >= 0 && position < dataList.size()) {
            dataList.set(position, city);
            cityAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Saved " + city.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {"Edmonton", "Vancouver", "Toronto"};
        String[] provinces = {"AB", "BC", "ON"};

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> {
            // Add mode:
            new AddCityFragment().show(getSupportFragmentManager(), "Add City");
        });

        // Edit on item click:
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            City city = dataList.get(position);
            AddCityFragment.newInstance(city.getName(), city.getProvince(), position)
                    .show(getSupportFragmentManager(), "Edit City");
        });
    }
}
