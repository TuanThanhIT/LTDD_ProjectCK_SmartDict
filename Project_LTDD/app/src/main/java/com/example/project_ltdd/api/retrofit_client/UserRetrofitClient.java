package com.example.project_ltdd.api.retrofit_client;

import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.api.services.WordService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRetrofitClient {
    private static final String BASE_URL = "http://192.168.87.22:8077/api/users/"; // Dùng 10.0.2.2 nếu chạy trên Android Emulator
    private static Retrofit retrofit;

    public static UserService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(UserService.class);
    }
}
