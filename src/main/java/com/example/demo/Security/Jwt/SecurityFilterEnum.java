package com.example.demo.Security.Jwt;


public enum SecurityFilterEnum {

    AUTHORIZATION("Authorization"),
    BEARER("Bearer ");

    private final String value;

    SecurityFilterEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
