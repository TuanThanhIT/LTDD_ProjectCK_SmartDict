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

    @GET("getWordsFolder/{folderId}")
    Call<List<WordModel>> getWordsFolder(@Path("folderId") int folderId);

    @GET("getFolderWord/{wordId}")
    Call<FolderModel> getFolderByWord(@Path("wordId") Long wordId);

    @DELETE("{userId}/deleteFavorWord/{wordId}")
    Call<Void> deleteWordFavor(@Path("userId") int userId, @Path("wordId") Long wordId);

    @GET("wordFavor/exists")
    Call<Boolean> checkFavorite(@Query("userId") int userId, @Query("wordId") Long wordId);

    @GET("folders/except")
    Call<List<FolderModel>> getFoldersExcept(@Query("userId") int userId, @Query("wordId") Long word);

    @PATCH("updateFolderWord")
    Call<ResponseBody> updateFolderWord(@Query("userId") int userId, @Query("wordId") Long wordId, @Query("folderId") int folderId);

    @PATCH("updateFolderWords")
    Call<ResponseBody> updateFolderWords(@Query("userId") int userId, @Query("folderId") int folderId, @Query("listWordIds") List<Long> listWordId);

    @DELETE("deleteFavorWords")
    Call<ResponseBody> deleteFavorWords(@Query("userId") int userId, @Query("listWordIds") List<Long> listWordId);

    @FormUrlEncoded
    @POST("addFavorWords")
    Call<ResponseBody> addOrUpdateFavorWords(@Field("userId") int userId, @Field("folderId") int folderId, @Field("listWords") List<Long> listWords);

    @DELETE("deleteSearchWords")
    Call<ResponseBody> deleteSearchWords(@Query("userId") int userId, @Query("listSearchWords") List<Long> listSearchWords);


}
