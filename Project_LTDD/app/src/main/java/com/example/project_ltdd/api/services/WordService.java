package com.example.project_ltdd.api.services;

import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.models.WordTopModel;

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

    @GET("top5WordFavors")
    Call<List<WordTopModel>> getTopWordsFavor();

    @GET("top5WordSearchs")
    Call<List<WordTopModel>> getTopWordsSearch();
}
