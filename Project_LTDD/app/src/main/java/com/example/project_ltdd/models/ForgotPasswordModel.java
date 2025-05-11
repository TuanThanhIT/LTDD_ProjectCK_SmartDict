package com.example.project_ltdd.models;

public class ForgotPasswordModel {
    private String email;

    public ForgotPasswordModel(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
