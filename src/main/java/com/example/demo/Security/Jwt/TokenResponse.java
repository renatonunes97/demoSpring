package com.example.demo.Security.Jwt;

public class TokenResponse {

    private String Token;

    public TokenResponse(String token) {
        Token = token;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
