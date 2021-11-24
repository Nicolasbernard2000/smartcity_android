package com.example.smartcity_app.repositories.web.dto;

import androidx.annotation.NonNull;

import com.squareup.moshi.Json;

import java.util.GregorianCalendar;

public class UserDto {
    private Integer id;
    private String email;
    private String password;

    @Json(name="first_name")
    private String firstName;

    @Json(name="last_name")
    private String lastName;

//    @Json(name="birth_date_name")
//    private GregorianCalendar birthDate;

    private String city;
    private String street;

    @Json(name="zip_code")
    private Integer zipCode;

    @Json(name="house_number")
    private Integer houseNumber;

    public UserDto(Integer id, String email, String password, String firstName, String lastName, String city, String street, Integer zipCode, Integer houseNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        //this.birthDate = birthDate;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email=" + email + '\'' +
                ", password=" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
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

//    public GregorianCalendar getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(GregorianCalendar birthDate) {
//        this.birthDate = birthDate;
//    }

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
}
