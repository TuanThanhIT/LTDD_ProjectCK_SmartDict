package com.example.project_ltdd.models;

import java.io.Serializable;
import java.util.List;

public class MeaningModel implements Serializable {
    private Long meaning_id;
    private String partOfSpeech;
    private String vietnameseMeaning;

    private List<DefinitionModel> definitions;

    // Getter & Setter


    public List<DefinitionModel> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<DefinitionModel> definitions) {
        this.definitions = definitions;
    }

    public Long getMeaningId() {
        return meaning_id;
    }

    public void setMeaningId(Long meaningId) {
        this.meaning_id = meaningId;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }

    public void setVietnameseMeaning(String vietnameseMeaning) {
        this.vietnameseMeaning = vietnameseMeaning;
    }

    public MeaningModel(Long meaningId, String vietnameseMeaning, String partOfSpeech) {
        this.meaning_id = meaningId;
        this.vietnameseMeaning = vietnameseMeaning;
        this.partOfSpeech = partOfSpeech;
    }

    public MeaningModel() {
    }
}