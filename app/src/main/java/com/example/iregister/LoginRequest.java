package com.example.iregister;

public class LoginRequest {
    private String str_email;
    private String password;

    public LoginRequest(String email, String password) {
        this.str_email = email;
        this.password = password;
    }
}
