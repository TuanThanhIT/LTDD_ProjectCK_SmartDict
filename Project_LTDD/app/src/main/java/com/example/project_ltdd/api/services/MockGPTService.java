package com.example.project_ltdd.api.services;

<<<<<<< HEAD
=======
/*import com.example.project_ltdd.api.responses.GPTResponse;*/
>>>>>>> dev_toan
import com.example.project_ltdd.models.GPTModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MockGPTService {
    @GET("smartdictGPT")
    Call<List<GPTModel>> getGptResponse(@Query("input") String input);

}
