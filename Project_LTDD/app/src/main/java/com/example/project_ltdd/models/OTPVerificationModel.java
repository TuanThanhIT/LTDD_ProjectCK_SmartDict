package com.example.project_ltdd.models;

public class OTPVerificationModel {
    private String email;
    private String otp;
    private String fullname;
    private String password;

    public OTPVerificationModel() {
    }

    public OTPVerificationModel(String email, String otp, String fullname, String password) {
        this.email = email;
        this.otp = otp;
        this.fullname = fullname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
