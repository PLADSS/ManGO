package com.example.mango;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddActivity extends AppCompatActivity {
    EditText title_input, author_input, pages_input;
    Button add_button, select_pdf_button;
    FloatingActionButton back_arrow_button;
    Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        add_button = findViewById(R.id.add_button);
        select_pdf_button = findViewById(R.id.select_pdf_button);
        back_arrow_button =findViewById(R.id.back_arrow_button);
        back_arrow_button.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        pdfUri = result.getData().getData();
                        Toast.makeText(this, "PDF Seçildi: " + pdfUri.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("PDF URI", pdfUri.toString());
                    } else {
                        Toast.makeText(this, "PDF Seçimi Başarısız", Toast.LENGTH_SHORT).show();
                        Log.d("PDF URI", "PDF selection failed or canceled");
                    }
                });

        select_pdf_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pdfPickerLauncher.launch(intent);
        });

        add_button.setOnClickListener(v -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
            String title = title_input.getText().toString().trim();
            String author = author_input.getText().toString().trim();
            int pages = Integer.parseInt(pages_input.getText().toString().trim());
            String pdfUriString = (pdfUri != null) ? pdfUri.toString() : null;

            long result = myDB.addBook(title, author, pages, pdfUriString);
            if (result == -1) {
                Toast.makeText(AddActivity.this, "Başarısız :(", Toast.LENGTH_SHORT).show();
                Log.e("DB Insert", "Failed to insert data");
            } else {
                Toast.makeText(AddActivity.this, "Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
                Log.d("DB Insert", "Data inserted successfully");
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.putExtra("pdfUri", pdfUriString);
                startActivity(intent);
                finish();
            }
        });
    }
}
