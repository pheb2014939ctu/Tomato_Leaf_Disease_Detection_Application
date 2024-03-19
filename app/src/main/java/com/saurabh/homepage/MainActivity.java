package com.saurabh.homepage;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.*;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;
    TextView instruction_info;
    TextView instruction_info_txt;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        instruction_info =findViewById(R.id.introduction_info);
        instruction_info.setVisibility(View.VISIBLE);






        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId())
                    {
                        case R.id.menu:
                            startActivity(new Intent(getApplicationContext(), menupage.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.home:
                            return true;

                        case R.id.note:
                            startActivity(new Intent(getApplicationContext(), NotesListActivity.class));
                            overridePendingTransition(0, 0);
                            return true;


                        case R.id.you:
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }

                    return false;
                }
            });


        TextView plant_Disease = findViewById(R.id.plantDisease);
                plant_Disease.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CamActive.class);
            startActivity(i);
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView instruction_info = findViewById(R.id.introduction_info);
        instruction_info.setOnClickListener((v -> {
            Intent i = new Intent(MainActivity.this, CamActive.class);
            startActivity(i);
        }));




        // Thay đổi GridLayoutManager thành LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        // Thiết lập RecyclerView với LayoutManager
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        // Tiếp theo là tiếp tục với code
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

        adapter = new MyAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);
    }




}