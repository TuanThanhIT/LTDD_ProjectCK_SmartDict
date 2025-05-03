package com.example.project_ltdd.models;

public class UserAnswerModel {

    private int question_id;
    private int answer_id;
    private int test_id;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public UserAnswerModel(int test_id, int question_id, int answer_id) {
        this.test_id = test_id;
        this.answer_id = answer_id;
        this.question_id = question_id;
    }
}
