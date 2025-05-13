package com.example.project_ltdd.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.AuthRetrofitClient;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.AuthService;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.OTPVerificationModel;
import com.example.project_ltdd.models.UserRegisterModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp;
    EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword;
    TextView tv_login;
    private String email, fullname, password, confirmPassword;

    AuthService authService;
    ProgressDialog progressDialog; // ProgressDialog

    private UserService userService = UserRetrofitClient.getClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Anhxa();

        authService = AuthRetrofitClient.getClient().create(AuthService.class);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);

        /*ĐĂNG KÝ VÀ TẠO OTP*/
        btnSignUp.setOnClickListener(v -> {
            fullname = edtFullName.getText().toString();
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();
            confirmPassword = edtConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            UserRegisterModel dto = new UserRegisterModel();
            dto.setEmail(email);
            dto.setFullname(fullname);
            dto.setPassword(password);

            authService.register(dto).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    // Tắt ProgressDialog
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "OTP đã gửi đến email", Toast.LENGTH_SHORT).show();
                        SendOTPSuccessful();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Tắt ProgressDialog
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        });

        tv_login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Toast.makeText(this, "Đăng kí", Toast.LENGTH_SHORT).show();
    }

    private void SendOTPSuccessful() {
        /*NHẬP OTP VÀ TẠO TÀI KHOẢN*/
        LayoutInflater inflater = LayoutInflater.from(SignUpActivity.this);
        View otpView = inflater.inflate(R.layout.activity_otp, null);

        EditText edtOtp = otpView.findViewById(R.id.otpView);

        AlertDialog dialog = new AlertDialog.Builder(SignUpActivity.this)
                .setView(otpView)
                .setCancelable(false)
                .setPositiveButton("Xác nhận", null)
                .setNegativeButton("Huỷ", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                String otpInput = edtOtp.getText().toString().trim();
                if (otpInput.length() != 6) {
                    edtOtp.setError("OTP phải có 6 chữ số");
                    return;
                }

                // Hiện ProgressDialog trước khi gọi API xác thực OTP
                progressDialog.show();

                OTPVerificationModel otpDTO = new OTPVerificationModel();
                otpDTO.setEmail(email);
                otpDTO.setOtp(otpInput);
                otpDTO.setFullname(fullname);
                otpDTO.setPassword(password);

                authService.verifyOtp(otpDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        // Tắt ProgressDialog
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "OTP không đúng hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // ✅ Tắt ProgressDialog
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
        /*NHẬP OTP VÀ TẠO TÀI KHOẢN*/
    }

    private void Anhxa() {
        btnSignUp = findViewById(R.id.btn_sign_up);
        edtFullName = findViewById(R.id.edt_full_name);
        edtEmail = findViewById(R.id.edt_email_su);
        edtPassword = findViewById(R.id.edt_password_su);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        tv_login = findViewById(R.id.tv_change_to_login_su);
    }
}
