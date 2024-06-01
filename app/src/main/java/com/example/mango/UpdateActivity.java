package com.example.mango;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateActivity extends AppCompatActivity {
    EditText title_input, author_input, pages_input;
    Button update_button, delete_button, select_pdf_button;
    FloatingActionButton back_arrow_button;
    String id, title, author, pages, pdfUri;
    com.example.mango.MyDatabaseHelper myDB;
    Uri newPdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input_update);
        author_input = findViewById(R.id.author_input_update);
        pages_input = findViewById(R.id.pages_input_update);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        select_pdf_button = findViewById(R.id.select_pdf_button_update);
        back_arrow_button =findViewById(R.id.back_arrow_button);
        back_arrow_button.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, com.example.mango.MainActivity.class);
            startActivity(intent);
        });

        myDB = new com.example.mango.MyDatabaseHelper(UpdateActivity.this);

        getAndSetIntentData();

        update_button.setOnClickListener(v -> updateData());

        delete_button.setOnClickListener(v -> confirmDialog());

        ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        newPdfUri = result.getData().getData();
                        Toast.makeText(UpdateActivity.this, "PDF Selected", Toast.LENGTH_SHORT).show();
                    }
                });

        select_pdf_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pdfPickerLauncher.launch(intent);
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") &&
                getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") &&
                getIntent().hasExtra("pages") &&
                getIntent().hasExtra("pdfUri")) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            pdfUri = getIntent().getStringExtra("pdfUri");

            title_input.setText(title);
            author_input.setText(author);
            pages_input.setText(pages);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void updateData() {
        title = title_input.getText().toString().trim();
        author = author_input.getText().toString().trim();
        pages = pages_input.getText().toString().trim();
        String finalPdfUri = (newPdfUri != null) ? newPdfUri.toString() : pdfUri;

        myDB.updateData(id, title, author, pages, finalPdfUri);
        Intent intent = new Intent(UpdateActivity.this, com.example.mango.MainActivity.class);
        startActivity(intent);
        finish();
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteOneRow(id);
                Intent intent = new Intent(UpdateActivity.this, com.example.mango.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.create().show();
    }
}
