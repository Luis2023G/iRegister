package com.example.iregister;

import com.example.iregister.User;

public class LoginResponse {
    private String token;

    private int user_id;

    private String email;

    private boolean is_docente;

    private boolean is_alumno;

    public String getToken() {
        return token;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isIs_docente() {
        return is_docente;
    }

    public boolean isIs_alumno() {
        return is_alumno;
    }
}
