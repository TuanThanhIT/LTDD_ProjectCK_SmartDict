package com.example.project_ltdd.models;

import java.io.Serializable;
import java.util.List;

public class WordModel implements Serializable {

    private Long word_id;
    private String word;
    private List<PhoneticModel> phonetics;
    private List<MeaningModel> meanings;

    public Long getWordId() {
        return word_id;
    }

    public void setWordId(Long wordId) {
        this.word_id = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<MeaningModel> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<MeaningModel> meanings) {
        this.meanings = meanings;
    }

    public List<PhoneticModel> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<PhoneticModel> phonetics) {
        this.phonetics = phonetics;
    }

    public WordModel(Long wordId, List<MeaningModel> meanings, List<PhoneticModel> phonetics, String word) {
        this.word_id = wordId;
        this.meanings = meanings;
        this.phonetics = phonetics;
        this.word = word;
    }

    public WordModel() {
    }
}
