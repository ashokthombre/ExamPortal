package com.example.examportal.model;

public class JwtToken {

    String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
