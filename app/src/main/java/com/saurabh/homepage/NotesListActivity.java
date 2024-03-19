package com.saurabh.homepage;

import static com.saurabh.homepage.MyDatabasesHelper.COLUMN_TIMESTAMP;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.annotation.SuppressLint;
import android.widget.ImageView;
import java.util.ArrayList;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabasesHelper myDB;
    ArrayList<String> note_id, note_title, note_desc, note_timestamp;
    CustomAdapter customAdapter;

    ImageView empty_imageView, empty_imageView1;
    TextView no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        recyclerView = findViewById(R.id.note_recyclerview);
        add_button = findViewById(R.id.add_button);
        empty_imageView = findViewById(R.id.empty_imageview);
        empty_imageView1 = findViewById(R.id.empty_imageview1);
        no_data = findViewById(R.id.no_data);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.note);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.note:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), menupage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.you:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });





        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesListActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        myDB = new MyDatabasesHelper(NotesListActivity.this);
        note_id = new ArrayList<>();
        note_title = new ArrayList<>();
        note_desc = new ArrayList<>();
        note_timestamp = new ArrayList<>(); // Initialize note_timestamp

        storeDataInArray();

        customAdapter = new CustomAdapter(NotesListActivity.this, this, note_id, note_title, note_desc, note_timestamp);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotesListActivity.this));
        customAdapter.setOnDeleteButtonClickListener(new CustomAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                // Call the deleteItem method when the delete button is clicked
                customAdapter.deleteItem(position);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    @SuppressLint("Range")
    void storeDataInArray() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageView.setVisibility(View.VISIBLE);
            empty_imageView1.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                note_id.add(cursor.getString(0));
                note_title.add(cursor.getString(1));
                note_desc.add(cursor.getString(2));
                note_timestamp.add(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));

            }
            empty_imageView1.setVisibility(View.GONE);
            empty_imageView.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }

    }








}
