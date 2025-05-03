package com.example.project_ltdd.models;

public class QuizHistoryModel {
    private String quizTitle;
    private int attempt;

    private int totalCorrectAnswer;
    private int testTime;
    private int totalQuestion;

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public int getTestTime() {
        return testTime;
    }

    public int getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    public void setTotalCorrectAnswer(int totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public QuizHistoryModel(String quizTitle, int totalQuestion, int testTime, int totalCorrectAnswer, int attempt) {
        this.quizTitle = quizTitle;
        this.totalQuestion = totalQuestion;
        this.testTime = testTime;
        this.totalCorrectAnswer = totalCorrectAnswer;
        this.attempt = attempt;
    }

    public QuizHistoryModel() {
    }
}
