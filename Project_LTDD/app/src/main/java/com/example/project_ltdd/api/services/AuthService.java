package com.example.project_ltdd.api.services;
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
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
