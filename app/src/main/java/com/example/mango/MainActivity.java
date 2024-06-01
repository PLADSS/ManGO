package com.example.mango;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton add_page_button,settings_button;

    ImageView empty_imageview;
    TextView no_data;

    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    ArrayList<String> pdfUris;
    com.example.mango.CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        empty_imageview = findViewById(R.id.empty_imageView);
        no_data = findViewById(R.id.no_data);

        //Menu buttonları
        add_page_button = findViewById(R.id.add_page_button);
        add_page_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.mango.AddActivity.class);
            startActivity(intent);});

        settings_button =findViewById(R.id.settings_button);
        settings_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.mango.SettingsActivity.class);
            startActivity(intent);});

        myDB = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        pdfUris = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new com.example.mango.CustomAdapter(MainActivity.this, book_id, book_title, book_author, book_pages, pdfUris);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
            Log.d("MainActivity", "No data found in the database");
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
                pdfUris.add(cursor.getString(4)); // Assume the PDF URI is stored as the 5th column
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
            Log.d("MainActivity", "Data retrieved from the database: " + book_id.size() + " items");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            Toast.makeText(this, "Deleting all records", Toast.LENGTH_SHORT).show();
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All")
                .setMessage("Are you sure you want to delete all records?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                    myDB.deleteAllData();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
