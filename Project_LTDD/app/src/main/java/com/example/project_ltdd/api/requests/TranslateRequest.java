package com.example.project_ltdd.api.requests;

public class TranslateRequest {
    private String q;
    private String source;
    private String target;
    private String format;

    public TranslateRequest(String q, String source, String target) {
        this.q = q;
        this.source = source;
        this.target = target;
        this.format = "text"; // Bắt buộc phải có
    }

    public String getQ() {
        return q;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getFormat() {
        return format;
    }
}

