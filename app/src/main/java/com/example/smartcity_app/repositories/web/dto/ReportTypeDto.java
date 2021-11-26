package com.example.smartcity_app.repositories.web.dto;

import androidx.annotation.NonNull;

public class ReportTypeDto {
    private Integer id;
    private String label;

    public ReportTypeDto(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ReportTypeDto{" +
                "id=" + id +
                ", label=" + label +
                "}";
    }
}
