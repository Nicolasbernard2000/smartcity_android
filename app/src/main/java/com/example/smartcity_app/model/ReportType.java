package com.example.smartcity_app.model;

public class ReportType {
    private Integer id;
    private String label;
    private String image;

    public ReportType(Integer id, String label, String image) {
        this.id = id;
        this.label = label;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ReportType{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
