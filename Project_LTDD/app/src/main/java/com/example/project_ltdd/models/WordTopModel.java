package com.example.project_ltdd.models;

public class WordTopModel {
    private Long word_id;
    private String word;

    public Long getWord_id() {
        return word_id;
    }

    public void setWord_id(Long word_id) {
        this.word_id = word_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordTopModel(String word, Long word_id) {
        this.word = word;
        this.word_id = word_id;
    }
}
