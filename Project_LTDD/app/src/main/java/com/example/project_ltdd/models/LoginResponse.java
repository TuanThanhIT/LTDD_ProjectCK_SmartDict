package com.example.project_ltdd.models;

public class LoginResponse {
    private boolean success;
    private String message;
    private Data data;

    public LoginResponse() {
    }

    public class Data {
        private int userId;
        private String name;
        private String email;

        public int getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
