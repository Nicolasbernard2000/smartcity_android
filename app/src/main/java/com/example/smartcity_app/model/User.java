package com.example.smartcity_app.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String city;
    private String street;
    private Integer zipCode;
    private Integer houseNumber;

    public User(String email, String password, String firstName, String lastName, Date birthDate, String city, String street, Integer zipCode, Integer houseNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
    }

    public User(String email, String password) {
        this(email, password, "Nicolas", "Bernard", new Date(2000, 12, 7), "Namur", "Rue de l'hotel de ville", 5000, 10);
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
}
