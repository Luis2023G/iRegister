package com.example.iregister;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("str_idAlumno")
    private String str_idAlumno;
    @SerializedName("str_nombres")
    private String str_nombres;
    @SerializedName("str_apellidos")
    private String str_apellidos;
    @SerializedName("dt_fecha_nacimiento")
    private String dt_fecha_nacimiento;
    @SerializedName("bool_activo")
    private boolean bool_activo;

    public String getEmail() {
        return email;
    }

    public boolean isBool_activo() {
        return bool_activo;
    }

    public String getDt_fecha_nacimiento() {
        return dt_fecha_nacimiento;
    }

    public String getStr_apellidos() {
        return str_apellidos;
    }

    public String getStr_nombres() {
        return str_nombres;
    }

    public String getStr_idAlumno() {
        return str_idAlumno;
    }

    @SerializedName("str_email")
    public String email;


}
