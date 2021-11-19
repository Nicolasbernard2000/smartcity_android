package com.example.smartcity_app.model;

public class ReportType {
    private Integer id;
    private String label;

    public ReportType(Integer id, String label) {
        setId(id);
        setLabel(label);
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
        return "ReportType{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
