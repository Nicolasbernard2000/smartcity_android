package com.example.smartcity_app.mappers;

import android.util.Log;

import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.dto.UserDto;

import java.io.Console;

public class UserMapper {
    private static UserMapper instance = null;

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        if(instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    public User mapToUser(UserDto dto) {
        if(dto == null)
            return null;

        try {
            User user = new User(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getCity(), dto.getStreet(), dto.getZipCode(), dto.getHouseNumber());
            return user;
        } catch (Exception e) {
            Log.v("DEBUG", "Erreur dans le mapping : " + e.getMessage());
            return null;
        }
    }

    public UserDto mapToUserDto(User user) {
        if(user == null)
            return null;
        UserDto userDto = new UserDto(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getCity(), user.getStreet(), user.getZipCode(), user.getHouseNumber());
        return userDto;
    }
}
