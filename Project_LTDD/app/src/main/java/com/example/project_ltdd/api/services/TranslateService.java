package com.example.project_ltdd.api.services;

import com.example.project_ltdd.api.requests.TranslateRequest;
import com.example.project_ltdd.api.responses.TranslateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface TranslateService {
    @Headers("Content-Type: application/json")
    @POST("translate")
    Call<TranslateResponse> translate(@Body TranslateRequest request);
}

