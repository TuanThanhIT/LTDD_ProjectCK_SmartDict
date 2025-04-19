package com.example.project_ltdd.models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionModel implements Serializable {

    private int questionId;
    private String questionText;
    private List<AnswerModel> options;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<AnswerModel> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerModel> options) {
        this.options = options;
    }

    public QuestionModel(int questionId, String questionText, List<AnswerModel> options) {
        this.options = options;
        this.questionText = questionText;
        this.questionId = questionId;
    }

    public QuestionModel() {
    }
}
