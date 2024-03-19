package com.saurabh.homepage;

public class DiseaseDetails {
    private String details;
    private String treatment;

    public DiseaseDetails(String details, String treatment) {
        this.details = details;
        this.treatment = treatment;
    }

    public String getDetails() {
        return details;
    }

    public String getTreatment() {
        return treatment;
    }
}
