package com.example.mango;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    ImageButton home_button, add_page_button, edit_button;
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tema tercihini uygulayın
        boolean isDarkMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("dark_mode_preference", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Menu butonları
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

        // Kullanıcı adı değiştirme butonu
        usernameTextView = findViewById(R.id.textView);
        edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(v -> showEditDialog());

        // Ayarlar fragmentını ekle
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
                .commit();
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kullanıcı Adını Değiştir");

        // Kullanıcıdan yeni adı almak için bir EditText ekleyin
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // "Kaydet" butonu ve onun tıklama olayını ekleyin
        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();
                usernameTextView.setText(newUsername);
            }
        });

        // "İptal" butonu ve onun tıklama olayını ekleyin
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
