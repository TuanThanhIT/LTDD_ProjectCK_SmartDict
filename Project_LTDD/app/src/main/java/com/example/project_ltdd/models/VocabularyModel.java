package com.example.project_ltdd.models;

public class VocabularyModel {
    private String word;
    private String phonetic;
    private String meaning;

    private String partOfSpeech;

    public VocabularyModel(String word, String phonetic, String meaning, String partOfSpeech) {
        this.word = word;
        this.phonetic = phonetic;
        this.meaning = meaning;
        this.partOfSpeech = partOfSpeech;
    }

    public VocabularyModel() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }


}
