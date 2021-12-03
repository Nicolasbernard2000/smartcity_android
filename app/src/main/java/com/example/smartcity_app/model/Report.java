package com.example.smartcity_app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class Report implements Serializable {
    private Integer id;
    private String description;
    private String state;
    private String city;
    private String street;
    private Integer zipCode;
    private Integer houseNumber;
    private Date creationDate;
    private Integer reporter;
    private ReportType reportType;
    public static final String DEFAULT_STATE = "pending";

    public Report(Integer id, String description, String state, String city, String street, Integer zipCode, Integer houseNumber, Date creationDate, Integer reporter, ReportType reportType) {
        this.id = id;
        this.description = description;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        this.creationDate = creationDate;
        this.reporter = reporter;
        this.reportType = reportType;
    }

    public Report(String description, String state, String city, String street, Integer zipCode, Integer houseNumber, Integer reporter, ReportType reportType) {
        this(null, description, state, city, street, zipCode, houseNumber, null, reporter, reportType);
        GregorianCalendar today = new GregorianCalendar();
        setCreationDate(new Date(today.getTimeInMillis()));
    }

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
        return "Report{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode=" + zipCode +
                ", houseNumber=" + houseNumber +
                ", creationDate='" + creationDate + '\'' +
                ", reporter=" + reporter +
                ", reportType=" + reportType +
                '}';
    }
}
