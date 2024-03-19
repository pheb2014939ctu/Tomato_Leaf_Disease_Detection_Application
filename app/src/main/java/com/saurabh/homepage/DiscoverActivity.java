package com.saurabh.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DiscoverActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;
    SearchView searchView;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(DiscoverActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        androidData = new DataClass("Tomato Late Blight", R.string.late_blight, R.drawable.late_blight_detail);
        dataList.add(androidData);
        androidData = new DataClass("Tomato Powdery Mildew", R.string.powdery_mildew, R.drawable.powdery_mildew);
        dataList.add(androidData);
        androidData = new DataClass("Tomato Leaf Spot", R.string.leaf_spot, R.drawable.leaf_spot);
        dataList.add(androidData);
        androidData = new DataClass("Tomato Yellow Leaf Curl Virus", R.string.yellow_leaf,  R.drawable.yellow_leaf);
        dataList.add(androidData);
        androidData = new DataClass("Tomato Spotted Spider Mites", R.string.spider_mites, R.drawable.spider_mites);

        dataList.add(androidData);
        adapter = new MyAdapter(DiscoverActivity.this, dataList);
        recyclerView.setAdapter(adapter);
    }

    private void searchList(String text) {
        List<DataClass> dataSearchList = new ArrayList<>();
        for (DataClass data : dataList) {
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}