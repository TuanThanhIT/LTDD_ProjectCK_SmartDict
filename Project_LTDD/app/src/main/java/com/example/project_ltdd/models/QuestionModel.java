package com.example.project_ltdd.models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionModel implements Serializable {

    private int question_id;
    private String question_test;
    private List<AnswerModel> listAnswers;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public List<AnswerModel> getListAnswers() {
        return listAnswers;
    }

    public void setListAnswers(List<AnswerModel> listAnswers) {
        this.listAnswers = listAnswers;
    }

    public String getQuestion_test() {
        return question_test;
    }

    public void setQuestion_test(String question_test) {
        this.question_test = question_test;
    }
}
