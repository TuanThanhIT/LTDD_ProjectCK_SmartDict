package com.example.project_ltdd.api.services;

import com.example.project_ltdd.models.QuestionModel;
import com.example.project_ltdd.models.QuizHistoryModel;
import com.example.project_ltdd.models.QuizModel;
import com.example.project_ltdd.models.TestModel;
import com.example.project_ltdd.models.UserAnswerModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuizService {

    @GET("getQuizzes")
    Call<List<QuizModel>> getQuizzes();

    @GET("{quizId}/getQuestionsAndAnswers")
    Call<List<QuestionModel>> getQuestionsAndAnswers(@Path("quizId") int quizId);

    @FormUrlEncoded
    @POST("addTest")
    Call<TestModel> addTest(@Field("userId") int userId, @Field("quizId") int quizId);

    @Headers("Content-Type: application/json")
    @POST("userAnswer")
    Call<ResponseBody> saveUserAnswer(@Body UserAnswerModel answer);


    @GET("getTestId")
    Call<Integer> getTestId(@Query("userId") int userId, @Query("quizId") int quizId);

    @FormUrlEncoded
    @POST("updateResult")
    Call<ResponseBody> updateResult(@Field("userId") int userId, @Field("quizId") int quizId, @Field("testTime") int testTime);

    @GET("getTestResult")
    Call<TestModel> getTestResult(@Query("userId") int userId, @Query("quizId") int quizId);

    @GET("getHistoryTest/{userId}")
    Call<List<QuizHistoryModel>> getHistoryTest(@Path("userId") int userId);
}

