package com.example.project_ltdd.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizModel implements Serializable {

    private String nameQuiz;
    private int questionCount;
    private int quizTime;

    private String quizImage;

    public String getQuizImage() {
        return quizImage;
    }

    public void setQuizImage(String quizImage) {
        this.quizImage = quizImage;
    }

    public QuizModel() {
    }

    public String getNameQuiz() {
        return nameQuiz;
    }

    public void setNameQuiz(String nameQuiz) {
        this.nameQuiz = nameQuiz;
    }

    public int getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(int quizTime) {
        this.quizTime = quizTime;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public QuizModel(String nameQuiz, int quizTime, int questionCount, String quizImage) {
        this.quizImage = quizImage;
        this.quizTime = quizTime;
        this.questionCount = questionCount;
        this.nameQuiz = nameQuiz;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
