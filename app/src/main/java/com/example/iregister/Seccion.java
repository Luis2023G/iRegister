package com.example.iregister;

import com.google.gson.annotations.SerializedName;

public class Seccion {

    @SerializedName("nombre")
    private String nombre;

    public String getNombre() {
        return nombre;
    }
}
