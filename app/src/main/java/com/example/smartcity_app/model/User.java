package com.example.smartcity_app.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.regex.Pattern;

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

    public User() {
        super();
    }

    public User(Integer id, String email, String password, String firstName, String lastName, Date birthDate, String role, String city, String street, Integer zipCode, Integer houseNumber) throws Exception {
        this.id = id;
        setEmail(email);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        setBirthDate(birthDate);
        this.role = role;
        this.city = city;
        this.street = street;
        setZipCode(zipCode);
        this.houseNumber = houseNumber;
    }

    public User(String email, String password, String firstName, String lastName, Date birthDate, String city, String street, Integer zipCode, Integer houseNumber) throws Exception {
        this(null, email, password, firstName, lastName, birthDate, "user", city, street, zipCode, houseNumber);
    }



    public void setEmail(String email) throws Exception {
        if(Pattern.matches("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            this.email = email;
        } else {
            throw new Exception("Ceci n'est pas une adresse mail");
        }
    }

    public void setZipCode(Integer zipCode) throws Exception {
        if(1000 <= zipCode && zipCode < 10000)
            this.zipCode = zipCode;
        else
            throw new Exception("Code postal incorrect");
    }

    public void setBirthDate(Date birthDate) throws Exception {
        Date today = new Date(System.currentTimeMillis());
        if(birthDate.before(today)) {
            this.birthDate = birthDate;
        } else {
            throw new Exception("La date de naissance ne peut pas être dans le futur");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
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

    public String getRole() {
        return role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
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
