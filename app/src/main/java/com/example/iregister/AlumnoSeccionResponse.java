package com.example.iregister;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlumnoSeccionResponse {
    @SerializedName("results")
    private List<AlumnoSeccion> results;

    public List<AlumnoSeccion> getResults() {
        return results;
    }
}

