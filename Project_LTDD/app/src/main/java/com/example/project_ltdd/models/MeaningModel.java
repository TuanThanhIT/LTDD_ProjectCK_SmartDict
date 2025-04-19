package com.example.project_ltdd.models;

import java.io.Serializable;
import java.util.List;

public class MeaningModel implements Serializable {
    private Long meaningId;
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
        return meaningId;
    }

    public void setMeaningId(Long meaningId) {
        this.meaningId = meaningId;
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
        this.meaningId = meaningId;
        this.vietnameseMeaning = vietnameseMeaning;
        this.partOfSpeech = partOfSpeech;
    }

    public MeaningModel() {
    }
}