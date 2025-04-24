package com.example.project_ltdd.api.services;

import com.example.project_ltdd.models.WordModel;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordService {
    @GET("all")
    Call<List<WordModel>> getAllWords(); // trả về danh sách

    @POST("add")
    Call<ResponseBody> addFolder(@Query("userId") int userId,
                                 @Query("folderName") String folderName);
}
