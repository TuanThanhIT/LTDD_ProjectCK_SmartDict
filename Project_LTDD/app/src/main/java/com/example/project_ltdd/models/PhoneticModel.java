package com.example.project_ltdd.models;

import java.io.Serializable;

public class PhoneticModel implements Serializable {
    private Long phoneticId;
    private String text;
    private String audio;

    // Getter & Setter

    public Long getPhoneticId() {
        return phoneticId;
    }

    public void setPhoneticId(Long phoneticId) {
        this.phoneticId = phoneticId;
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
        this.phoneticId = phoneticId;
        this.text = text;
    }

    public PhoneticModel() {
    }
}