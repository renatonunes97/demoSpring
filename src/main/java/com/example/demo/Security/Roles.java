package com.example.demo.Security;

public enum Roles {
    
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");


    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}


