package com.example.project_ltdd.models;

public class FavoriteWordModel {
    private int id;

    private int user_id;

    private int folder_id;

    private int word_id;

    private String wordName;

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public int getWord_id() {
        return word_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public FavoriteWordModel(int id, int word_id, int folder_id, int user_id, String wordName) {
        this.id = id;
        this.word_id = word_id;
        this.folder_id = folder_id;
        this.user_id = user_id;
        this.wordName = wordName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FavoriteWordModel(int id, int word_id, int folder_id, int user_id) {
        this.id = id;
        this.word_id = word_id;
        this.folder_id = folder_id;
        this.user_id = user_id;
    }

    public FavoriteWordModel() {
    }
}