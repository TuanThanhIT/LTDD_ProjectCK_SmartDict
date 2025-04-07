package com.example.project_ltdd.domain;

public class AdminQuizzDomain {
    private String title;
    private String content;
    private String picPath;

    public AdminQuizzDomain(String title, String content, String picPath) {
        this.title = title;
        this.content = content;
        this.picPath = picPath;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
