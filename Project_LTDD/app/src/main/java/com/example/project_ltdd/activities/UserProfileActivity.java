package com.example.project_ltdd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;
import com.example.project_ltdd.utils.UserPrefs;

public class UserProfileActivity extends AppCompatActivity {
    Button btn_backToMainScreen, btn_ChangPassword, btn_Logout, btn_Login;
    TextView txv_fullname;

    UserPrefs userPrefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userPrefs = new UserPrefs(this);

        btn_backToMainScreen = (Button) findViewById(R.id.btn_back_to_home_in_us_profile);
        btn_ChangPassword = (Button) findViewById(R.id.btn_change_password_pf);
        btn_Logout = findViewById(R.id.btn_logout);
        btn_Login = findViewById(R.id.btn_login);
        txv_fullname = findViewById(R.id.txv_fullname);

        if(userPrefs.isLoggedIn()){
            btn_Logout.setVisibility(View.VISIBLE);
            btn_Login.setVisibility(View.GONE);
        }

        btn_backToMainScreen.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btn_ChangPassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPrefs.clear();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        txv_fullname.setText(userPrefs.getFullName());

        Toast.makeText(this, "Tài khoản", Toast.LENGTH_SHORT).show();
    }
}
