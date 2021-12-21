package com.example.smartcity_app.repository.web.dto;

public class ReportTypeDto {
    private Integer id;
    private String label;
    private String image;

    public ReportTypeDto(Integer id, String label, String image) {
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

    public void setImage(String imageURL) {
        this.image = imageURL;
    }

    @Override
    public String toString() {
        return "ReportTypeDto{" +
                "id=" + id +
                ", label=" + label +
                ", image=" + image +
                "}";
    }
}
