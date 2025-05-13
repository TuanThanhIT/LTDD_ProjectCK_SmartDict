package com.example.project_ltdd.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;

public class UserProfileActivity extends AppCompatActivity {
    Button btn_backToMainScreen, btn_ChangPassword, btn_Logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        btn_backToMainScreen = (Button) findViewById(R.id.btn_back_to_home_in_us_profile);
        btn_ChangPassword = (Button) findViewById(R.id.btn_change_password_pf);
        btn_Logout = (Button) findViewById(R.id.btn_logout);
        btn_backToMainScreen.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btn_ChangPassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        btn_Logout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("usLoginFullname");
            editor.remove("usLoginId");
            editor.apply();
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }
}
