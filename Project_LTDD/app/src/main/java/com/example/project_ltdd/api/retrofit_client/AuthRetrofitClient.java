package com.example.project_ltdd.api.retrofit_client;

import com.example.project_ltdd.api.services.AuthService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AuthRetrofitClient {

    private static final String BASE_URL = "http://192.168.87.22:8077/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create()) // <- Đặt trước
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static AuthService getApiService() {
        return getClient().create(AuthService.class);
    }
}
