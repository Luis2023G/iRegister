package com.example.iregister;

public class VerificarQrRequest {
    private String str_codigo;
    private String str_idAlumno;

    public VerificarQrRequest(String str_codigo, String str_idAlumno) {
        this.str_codigo = str_codigo;
        this.str_idAlumno = str_idAlumno;
    }
}

