package com.example.project_ltdd.models;

import java.io.Serializable;

public class PhoneticModel implements Serializable {
    private Long phonetic_id;
    private String text;
    private String audio;

    // Getter & Setter

    public Long getPhoneticId() {
        return phonetic_id;
    }

    public void setPhoneticId(Long phoneticId) {
        this.phonetic_id = phoneticId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public PhoneticModel(String audio, Long phoneticId, String text) {
        this.audio = audio;
        this.phonetic_id = phoneticId;
        this.text = text;
    }

    public PhoneticModel() {
    }
}