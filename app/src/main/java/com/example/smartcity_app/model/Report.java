package com.example.smartcity_app.model;

import java.io.Serializable;

public class Report implements Serializable {
    private Integer id;
    private String description;
    private String status;
    private String city;
    private String street;
    private Integer zipCode;
    private Integer houseNumber;
    private String creationDate; //TODO passer en date
    private String type;

    public Report(Integer id, String description, String status, String city, String street, Integer zipCode, Integer houseNumber, String creationDate, String type) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        this.creationDate = creationDate;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getType() {
        return type;
    }
}
