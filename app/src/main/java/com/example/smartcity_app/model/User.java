package com.example.smartcity_app.model;

import androidx.annotation.NonNull;

import com.example.smartcity_app.R;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class User implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private GregorianCalendar birthDate;
    private String city;
    private String street;
    private Integer zipCode;
    private Integer houseNumber;

    public User(String email, String password, String firstName, String lastName, String city, String street, Integer zipCode, Integer houseNumber) throws Exception {
        setEmail(email);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        //setBirthDate(birthDate);
        this.city = city;
        this.street = street;
        setZipCode(zipCode);
        this.houseNumber = houseNumber;
    }

    public User(String email, String password) throws Exception {
        this(email, password, "Nicolas", "Bernard", "Namur", "Rue de l'hotel de ville", 5000, 10);
    }

    @Override
    public String toString() {
        return "Ceci est mon objet user";
    }

    public void setEmail(String email) throws Exception {
        if(Pattern.matches("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            this.email = email;
        } else {
            throw new Exception(R.string.exception_email + "");
        }
    }

    public void setZipCode(Integer zipCode) throws Exception {
        if(1000 <= zipCode && zipCode < 10000)
            this.zipCode = zipCode;
        else
            throw new Exception(R.string.exception_zip_code + "");
    }

    public void setBirthDate(GregorianCalendar birthDate) throws Exception {
        GregorianCalendar today = new GregorianCalendar();
        if(birthDate.after(today)) {
            throw new Exception(R.string.exception_birthdate + "");
        } else {
            this.birthDate = birthDate;
        }
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

    public GregorianCalendar getBirthDate() {
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
