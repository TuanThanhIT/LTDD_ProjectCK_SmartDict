package com.example.project_ltdd.models;

public class UserRegisterModel {
    private String email;
    private String fullname;
    private String password;

    public UserRegisterModel() {
    }

    public UserRegisterModel(String email, String fullname, String password) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
    }

    public String getEmail() {
        return email;
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

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
