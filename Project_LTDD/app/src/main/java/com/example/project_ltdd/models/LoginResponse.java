package com.example.project_ltdd.models;

public class LoginResponse {
    private boolean success;
    private String message;
    private Data data;

    public LoginResponse() {
    }

    public class Data {
        private int userId;
        private String fullname;
        private String email;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getUserId() {
            return userId;
        }

        public String getFullName() {
            return fullname;
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
