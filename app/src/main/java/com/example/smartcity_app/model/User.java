package com.example.smartcity_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String role;
    private String city;
    private String street;
    private Integer zipCode;
    private Integer houseNumber;

    public User(Integer id, String email, String password, String firstName, String lastName, Date birthDate, String role, String city, String street, Integer zipCode, Integer houseNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
    }

    public User(String email, String password, String firstName, String lastName, Date birthDate, String city, String street, Integer zipCode, Integer houseNumber) {
        this(null, email, password, firstName, lastName, birthDate, "user", city, street, zipCode, houseNumber);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", email=" + email + '\'' +
                ", password=" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate + '\'' +
                ", role=" + role + '\'' +
                ", city=" + city + '\'' +
                ", street=" + street + '\'' +
                ", zipCode=" + zipCode + '\'' +
                ", houseNumber=" + houseNumber +
                '}';
    }
}
