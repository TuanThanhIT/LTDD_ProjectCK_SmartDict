package com.example.project_ltdd.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.AuthRetrofitClient;
import com.example.project_ltdd.api.services.AuthService;
import com.example.project_ltdd.databinding.ActivityLoginBinding;
import com.example.project_ltdd.models.LoginRequest;
import com.example.project_ltdd.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static String usLoginFullname;
    public static int usLoginId;
    private ActivityLoginBinding binding;
    EditText edt_Email, edt_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation bottom_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

        binding.topLinearLayout.setAnimation(bottom_down);

        Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                binding.cardViewLogo.setAnimation(fade_in);
                binding.textViewLogo.setAnimation(fade_in);
                binding.cardViewContain.setAnimation(fade_in);
                binding.registerLayout.setAnimation(fade_in);
                binding.cardView2.setAnimation(fade_in);
                binding.cardView3.setAnimation(fade_in);
                binding.cardView4.setAnimation(fade_in);
            }
        };


        handler.postDelayed(runnable, 1000);

        AnhXa();

        binding.btnLogin.setOnClickListener(v -> {
            String email = edt_Email.getText().toString().trim();
            String password = edt_Password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);

            AuthService apiService = AuthRetrofitClient.getClient().create(AuthService.class);
            Call<LoginResponse> call = apiService.login(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse res = response.body();
                        if (res.isSuccess()) {
                            usLoginFullname = res.getData().getFullName();
                            usLoginId = res.getData().getUserId();
                            Toast.makeText(LoginActivity.this, "Welcome " + usLoginFullname, Toast.LENGTH_SHORT).show();

                            // TODO: Chuyển sang màn hình chính
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void AnhXa(){
        edt_Email = (EditText) findViewById(R.id.edt_email);
        edt_Password = (EditText) findViewById(R.id.edt_password);

    }
}
