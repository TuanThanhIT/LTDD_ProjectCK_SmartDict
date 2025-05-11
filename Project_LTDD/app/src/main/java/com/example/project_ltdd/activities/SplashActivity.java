package com.example.project_ltdd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng SplashActivity
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hiển thị layout splash
        setContentView(R.layout.activity_splash);

        // Áp dụng animation
        ImageView logo = findViewById(R.id.logo);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView txv_appName = findViewById(R.id.txv_appName);
        TextView txv_slogan = findViewById(R.id.txv_slogan);
        Button btn_skip = findViewById(R.id.btn_skip);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.move);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.move_blink);
        logo.startAnimation(animation);
        txv_slogan.startAnimation(animation2);
        txv_appName.startAnimation(animation1);


        // Delay 2 giây rồi vào MainActivity
        handler.postDelayed(runnable, 2000);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hủy delay tự động
                handler.removeCallbacks(runnable);

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
