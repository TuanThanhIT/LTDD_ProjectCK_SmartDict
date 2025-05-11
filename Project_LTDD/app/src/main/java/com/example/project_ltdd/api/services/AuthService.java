package com.example.project_ltdd.api.services;
import com.example.project_ltdd.models.ChangePasswordModel;
import com.example.project_ltdd.models.ForgotPasswordModel;
import com.example.project_ltdd.models.LoginRequest;
import com.example.project_ltdd.models.LoginResponse;
import com.example.project_ltdd.models.OTPVerificationModel;
import com.example.project_ltdd.models.UserRegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/register")
    Call<String> register(@Body UserRegisterModel dto);

    @POST("/api/auth/verify-otp")
    Call<String> verifyOtp(@Body OTPVerificationModel dto);
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("/api/auth/forgot-password")
    Call<String> forgotPassword(@Body ForgotPasswordModel request);

    @POST("/api/auth/change-password")
    Call<String> changePassword(@Body ChangePasswordModel request);

}
