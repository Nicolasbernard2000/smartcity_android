package com.example.smartcity_app.service.mappers;

import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repository.web.dto.UserDto;

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
            User user = new User(dto.getId(), dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getBirthDate(), dto.getRole(), dto.getCity(), dto.getStreet(), dto.getZipCode(), dto.getHouseNumber());
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public UserDto mapToUserDto(User user) {
        if(user == null)
            return null;
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getRole(), user.getCity(), user.getStreet(), user.getZipCode(), user.getHouseNumber());

        return userDto;
    }
}
