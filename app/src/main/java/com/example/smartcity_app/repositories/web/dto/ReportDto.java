package com.example.smartcity_app.repositories.web.dto;

import com.example.smartcity_app.model.ReportType;
import com.squareup.moshi.Json;

import java.util.Date;

public class ReportDto {
    private Integer id;

    private String description;
    private String state;
    private String city;
    private String street;

    @Json(name = "zip_code")
    private Integer zipCode;

    @Json(name = "house_number")
    private Integer houseNumber;

    @Json(name = "created_at")
    private Date creationDate;

    private Integer reporter;

    @Json(name = "report_type")
    private ReportType reportType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getReporter() {
        return reporter;
    }

    public void setReporter(Integer reporter) {
        this.reporter = reporter;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode=" + zipCode +
                ", houseNumber=" + houseNumber +
                ", creationDate=" + creationDate +
                ", reporter=" + reporter +
                ", reportType=" + reportType +
                '}';
    }
}
