package com.example.smartcity_app.repositories.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
import java.sql.Date;

public class UserDto {
    private Integer id;
    private String email;
    private String password;

    @Json(name="first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Json(name="last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Json(name="birth_date")
    @JsonProperty("birth_date")
    private Date birthDate;

    private String role;
    private String city;
    private String street;

    @Json(name="zip_code")
    @JsonProperty("zip_code")
    private Integer zipCode;

    @Json(name="house_number")
    @JsonProperty("house_number")
    private Integer houseNumber;

    public UserDto(Integer id, String email, String password, String firstName, String lastName, Date birthDate, String role, String city, String street, Integer zipCode, Integer houseNumber) {
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

    public UserDto() {
        super();
    }

    @Override
    public String toString() {
        return "BackOfficeUser{" +
                "id=" + id +
                ", email=" + email + '\'' +
                ", password=" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", role=" + role +
                ", city=" + city +
                ", street=" + street +
                ", zipCode=" + zipCode +
                ", houseNumber=" + houseNumber +
                '}';
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

    public String getRole() {
        return role;
    }
}
