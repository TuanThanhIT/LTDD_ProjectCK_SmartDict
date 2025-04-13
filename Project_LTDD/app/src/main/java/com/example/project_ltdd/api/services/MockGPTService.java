package com.example.project_ltdd.api.services;

import com.example.project_ltdd.api.responses.GPTResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MockGPTService {
    @GET("smartdictGPT")
    Call<List<GPTResponse>> getGptResponse(@Query("input") String input);

}
