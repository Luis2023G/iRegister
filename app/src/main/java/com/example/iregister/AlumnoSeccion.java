package com.example.iregister;

import com.google.gson.annotations.SerializedName;

public class AlumnoSeccion {
    @SerializedName("int_idAlumnoSeccion")
    private int idAlumnoSeccion;

    @SerializedName("str_idAlumno")
    private String idAlumno;

    @SerializedName("int_idSeccion")
    private int idSeccion;

    @SerializedName("bool_activo")
    private boolean activo;

    public int getIdAlumnoSeccion() { return idAlumnoSeccion; }
    public String getIdAlumno() { return idAlumno; }
    public int getIdSeccion() { return idSeccion; }
    public boolean isActivo() { return activo; }
}

