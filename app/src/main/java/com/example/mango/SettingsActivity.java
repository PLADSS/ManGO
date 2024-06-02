package com.example.mango;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.google.android.material.imageview.ShapeableImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    ImageButton home_button, add_page_button, edit_button;
    TextView usernameTextView;
    ShapeableImageView imageView2;
    CheckBox permissionCheckbox, permissionCheckbox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme preference
        boolean isDarkMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("dark_mode_preference", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Menu buttons
        home_button = findViewById(R.id.home_button);
        home_button.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, com.example.mango.MainActivity.class);
            startActivity(intent);
        });

        add_page_button = findViewById(R.id.add_page_button);
        add_page_button.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, com.example.mango.AddActivity.class);
            startActivity(intent);
        });

        // Username change button
        usernameTextView = findViewById(R.id.textView);
        edit_button = findViewById(R.id.edit_button);
        imageView2 = findViewById(R.id.imageView2);

        edit_button.setOnClickListener(v -> {
            showEditDialog();
            selectImage();
        });

        // Checkbox for permission
        permissionCheckbox = findViewById(R.id.permission_checkbox);
        permissionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show();
            }
        });
        permissionCheckbox1 = findViewById(R.id.permission_checkbox1);
        permissionCheckbox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show();
            }
        });

        // Add settings fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
                .commit();
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kullanıcı Adını Değiştir");

        // Add an EditText to get the new username from the user
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Add "Save" button and its click event
        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();
                usernameTextView.setText(newUsername);
            }
        });

        // Add "Cancel" button and its click event
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageView2.setImageURI(selectedImage);
        }
    }



    /*      IZIN ISTEME BÖLÜMÜ


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to select image
                selectImage();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Dosya erişimi izni verilmedi", Toast.LENGTH_SHORT).show();
            }

     */
}
