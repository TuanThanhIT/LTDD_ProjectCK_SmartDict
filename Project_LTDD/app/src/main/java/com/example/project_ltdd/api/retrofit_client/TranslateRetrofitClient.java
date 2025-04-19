package com.example.project_ltdd.api.retrofit_client;

import com.example.project_ltdd.api.services.TranslateService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslateRetrofitClient {
    private static final String BASE_URL = "https://libretranslate.com/";
    private static Retrofit retrofit;

    public static TranslateService getClient() {
        if(retrofit == null){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson để chuyển JSON ↔ Java
                .build();
        }
        return retrofit.create(TranslateService.class);
    }
}
