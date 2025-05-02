package com.example.project_ltdd.models;

public class TestModel {
    private int id;
    private int test_time;
    private int attempt;
    private int total_correct_answer;
    private int user_id;
    private int quiz_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTotal_correct_answer() {
        return total_correct_answer;
    }

    public void setTotal_correct_answer(int total_correct_answer) {
        this.total_correct_answer = total_correct_answer;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getTest_time() {
        return test_time;
    }

    public void setTest_time(int test_time) {
        this.test_time = test_time;
    }
}
