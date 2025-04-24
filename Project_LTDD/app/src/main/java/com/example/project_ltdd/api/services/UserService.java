package com.example.project_ltdd.api.services;

import android.icu.text.CaseMap;

import com.example.project_ltdd.models.FavoriteWordModel;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.WordModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @POST("addWordSearch")
    Call<ResponseBody> addWordSearch(
            @Query("userId") int userId,
            @Query("wordId") long wordId
    );

    @GET("{userId}/wordLookedUp")
    Call<List<WordModel>> getWordLookedUp(@Path("userId") int userId);


    @FormUrlEncoded
    @POST("addFolder")
    Call<FolderModel> addFolder(@Field("userId") int userId,
                                  @Field("folderName") String folderName);

    @GET("{userId}/getFolders")
    Call<List<FolderModel>> getFolders(@Path("userId") int userId);

    @DELETE("deleteFolder/{folderId}")
    Call<Void> deleteFolder(@Path("folderId") int folderId);

    @FormUrlEncoded
    @PATCH("updateFolder")
    Call<FolderModel> updateFolder(@Field("folderId") int folderId,
                                   @Field("folderName") String folderName);

    @FormUrlEncoded
    @POST("addFavoriteWord")
    Call<FavoriteWordModel> addFavoriteWord(@Field("userId") int userId, @Field("folderId") int folderId, @Field("wordId") long wordId);

}
