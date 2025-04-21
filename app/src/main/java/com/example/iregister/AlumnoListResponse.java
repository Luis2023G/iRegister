package com.example.iregister;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AlumnoListResponse {
    @SerializedName("results")
    private List<AlumnoResponse> results;

    public List<AlumnoResponse> getResults() {
        return results;
    }

    public void setResults(List<AlumnoResponse> results) {
        this.results = results;
    }
}
