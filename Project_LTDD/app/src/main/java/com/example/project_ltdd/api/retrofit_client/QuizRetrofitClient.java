package com.example.project_ltdd.api.retrofit_client;

import com.example.project_ltdd.api.services.QuizService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizRetrofitClient {
    private static final String BASE_URL = "http://172.16.31.196:8077/api/quizzes/"; // Dùng 10.0.2.2 nếu chạy trên Android Emulator
    private static Retrofit retrofit;

    public static QuizService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(QuizService.class);
    }
}
