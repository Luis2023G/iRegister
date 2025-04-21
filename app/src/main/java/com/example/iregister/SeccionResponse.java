package com.example.iregister;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeccionResponse {

    @SerializedName("results")
    private List<Seccion> results;

    public List<Seccion> getResults() {
        return results;
    }
}
