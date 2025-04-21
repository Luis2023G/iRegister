package com.example.iregister;

public class VerificarQrResponse {
    private int int_idAsistencia;
    private int int_idAlumnoSeccion;
    private int int_idSesionClase;
    private boolean bool_activo;
    private String dt_fecha;

    public String getDt_hora_registro() {
        return dt_hora_registro;
    }

    private String dt_hora_registro;

    // Getters
    public int getInt_idAsistencia() { return int_idAsistencia; }
    public boolean isBool_asistio() { return bool_activo; }
    public String getDt_fecha_registro() { return dt_fecha; }
}

