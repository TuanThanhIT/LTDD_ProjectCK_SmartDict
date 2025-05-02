package com.example.project_ltdd.models;

public class AnswerModel {
    private int answerId;
    private String answerText;
    private boolean isCorrect;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public AnswerModel(int answerId, boolean isCorrect, String answerText) {
        this.answerId = answerId;
        this.isCorrect = isCorrect;
        this.answerText = answerText;
    }

    public AnswerModel() {
    }
}
