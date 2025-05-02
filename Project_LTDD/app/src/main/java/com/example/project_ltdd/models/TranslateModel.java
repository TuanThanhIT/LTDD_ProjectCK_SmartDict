package com.example.project_ltdd.models;

import com.google.gson.annotations.SerializedName;

public class TranslateModel {
    // Dùng cho gửi request
    private String q;
    private String source;
    private String target;
    private String format = "text"; // default

    // Dùng cho nhận response
    @SerializedName("translatedText")
    private String translatedText;

    // Constructors
    public TranslateModel(String q, String source, String target) {
        this.q = q;
        this.source = source;
        this.target = target;
    }

    // Getters
    public String getQ() {
        return q;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getFormat() {
        return format;
    }

    public String getTranslatedText() {
        return translatedText;
    }
}
