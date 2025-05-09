package com.example.project_ltdd.models;

public class UserTopModel {
    private int user_id;
    private String fullname;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public UserTopModel(int user_id, String fullname) {
        this.user_id = user_id;
        this.fullname = fullname;
    }
}
