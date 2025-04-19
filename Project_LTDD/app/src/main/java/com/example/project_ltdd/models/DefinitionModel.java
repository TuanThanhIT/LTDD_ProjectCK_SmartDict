package com.example.project_ltdd.models;

public class DefinitionModel {
    private Long definition_id;

    private String definiton;
    private String example;

    public Long getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(Long definition_id) {
        this.definition_id = definition_id;
    }

    public String getDefiniton() {
        return definiton;
    }

    public void setDefiniton(String definiton) {
        this.definiton = definiton;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public DefinitionModel(Long definition_id, String example, String definiton) {
        this.definition_id = definition_id;
        this.example = example;
        this.definiton = definiton;
    }
}
