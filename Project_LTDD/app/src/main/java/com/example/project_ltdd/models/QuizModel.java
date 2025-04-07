package com.example.project_ltdd.models;

public class QuizModel {

    private String nameQuiz;
    private int questionCount;
    private int quizTime;

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

    public QuizModel(String nameQuiz, int quizTime, int questionCount) {
        this.nameQuiz = nameQuiz;
        this.quizTime = quizTime;
        this.questionCount = questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
