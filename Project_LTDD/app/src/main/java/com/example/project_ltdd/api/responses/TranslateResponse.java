package com.example.project_ltdd.api.responses;

import com.google.gson.annotations.SerializedName;

public class TranslateResponse {
    @SerializedName("translatedText")
    private String translatedText;

    public String getTranslatedText() {
        return translatedText;
    }
}