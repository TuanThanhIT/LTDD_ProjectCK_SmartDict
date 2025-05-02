package com.example.project_ltdd.api.services;

<<<<<<< HEAD
=======
/*import com.example.project_ltdd.api.requests.TranslateRequest;
import com.example.project_ltdd.api.responses.TranslateResponse;*/
>>>>>>> dev_toan
import com.example.project_ltdd.models.TranslateModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface TranslateService {
    @POST("translate")
    Call<TranslateModel> translate(@Body TranslateModel body);
}

