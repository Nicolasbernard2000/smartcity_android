package com.example.smartcity_app.utils;

import java.sql.Date;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCheck {
    public static Boolean isInputValid(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static Boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return isInputValid(email) && matcher.matches();
    }

    public static Boolean isPasswordValid(String password, String confirmPassword) {
        return isInputValid(password) && isInputValid(confirmPassword) && password.equals(confirmPassword);
    }

    public static Boolean isBirthDateValid(String birthDate) {
        GregorianCalendar today = new GregorianCalendar();
        if(isInputValid(birthDate)) {
            List<String> date = Arrays.asList(birthDate.split("/"));
            int year = Integer.parseInt(date.get(2));
            int month = Integer.parseInt(date.get(1));
            int day = Integer.parseInt(date.get(0));
            GregorianCalendar birthDateCalendar = new GregorianCalendar(year, month, day);
            return birthDateCalendar.before(today);
        } else {
            return false;
        }
    }

    public static Boolean isHouseNumberValid(String houseNumber) {
        if(isInputValid(houseNumber)) {
            try {
                Integer houseNumberInteger = Integer.parseInt(houseNumber);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Boolean isZipCodeValid(String zipCode) {
        if(isInputValid(zipCode)) {
            try {
                Integer zipCodeInteger = Integer.parseInt(zipCode);
                return zipCodeInteger >= 1000 && zipCodeInteger <= 9999;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
