package com.example.project_ltdd.models;

public class QuizHistoryModel {
    private String quizName;
    private int quizTime;
    private int quizPoint;
    private int quzAttempt;

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getQuzAttempt() {
        return quzAttempt;
    }

    public void setQuzAttempt(int quzAttempt) {
        this.quzAttempt = quzAttempt;
    }

    public int getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(int quizTime) {
        this.quizTime = quizTime;
    }

    public int getQuizPoint() {
        return quizPoint;
    }

    public void setQuizPoint(int quizPoint) {
        this.quizPoint = quizPoint;
    }

    public QuizHistoryModel(String quizName, int quzAttempt, int quizPoint, int quizTime) {
        this.quizName = quizName;
        this.quzAttempt = quzAttempt;
        this.quizPoint = quizPoint;
        this.quizTime = quizTime;
    }

    public QuizHistoryModel() {
    }
}
