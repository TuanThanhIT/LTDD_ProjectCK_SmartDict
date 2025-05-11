package com.example.project_ltdd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.AuthRetrofitClient;
import com.example.project_ltdd.api.services.AuthService;
import com.example.project_ltdd.models.ChangePasswordModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edt_email_cpw, edt_pass_cpw, edt_new_pass;
    Button btn_change_pass;
    ImageButton btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AnhXa();

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePasswordActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        btn_change_pass.setOnClickListener(v -> {
            String email = edt_email_cpw.getText().toString().trim();
            String currentPassword = edt_pass_cpw.getText().toString().trim();
            String newPassword = edt_new_pass.getText().toString().trim();

            if (email.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ChangePasswordModel request = new ChangePasswordModel(email, currentPassword, newPassword);

            AuthService apiService = AuthRetrofitClient.getClient().create(AuthService.class);
            Call<String> call = apiService.changePassword(request);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Thất bại: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

    }

    void AnhXa(){
        edt_email_cpw = (EditText) findViewById(R.id.edt_email_cpw);
        edt_pass_cpw = (EditText) findViewById(R.id.edt_password_rspw);
        edt_new_pass = (EditText) findViewById(R.id.edt_new_password);
        btn_back = (ImageButton) findViewById(R.id.btn_back_spw);
        btn_change_pass = (Button) findViewById(R.id.btn_change_password);
    }
}
