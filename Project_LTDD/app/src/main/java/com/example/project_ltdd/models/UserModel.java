package com.example.project_ltdd.models;

import java.util.List;

public class UserModel {

    private int user_id;
    private String fullname;
    private String email;
    private String password;
    private String avatar;
    private List<TestModel> tests;
    private List<WordModel> words;
    private List<FolderModel> folders;
    private List<FavoriteWordModel> favoriteWords;

    // Getter and Setter methods

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<TestModel> getTests() {
        return tests;
    }

    public void setTests(List<TestModel> tests) {
        this.tests = tests;
    }

    public List<WordModel> getWords() {
        return words;
    }

    public void setWords(List<WordModel> words) {
        this.words = words;
    }

    public List<FolderModel> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderModel> folders) {
        this.folders = folders;
    }

    public List<FavoriteWordModel> getFavoriteWords() {
        return favoriteWords;
    }

    public void setFavoriteWords(List<FavoriteWordModel> favoriteWords) {
        this.favoriteWords = favoriteWords;
    }

    // Constructors

    public UserModel() {}

    public UserModel(int user_id, String fullname, String email, String password, String avatar,
                     List<TestModel> tests, List<WordModel> words, List<FolderModel> folders,
                     List<FavoriteWordModel> favoriteWords) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.tests = tests;
        this.words = words;
        this.folders = folders;
        this.favoriteWords = favoriteWords;
    }
}

