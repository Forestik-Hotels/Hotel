package com.hotels;

import com.hotels.dto.UserDto;
import com.hotels.entity.User;
import com.hotels.enums.Role;

public class ModelUtils {

    public static User getUser() {
        return User.builder()
            .id(1L)
            .firstName("Test")
            .lastName("Test")
            .profilePicturePath("test")
            .role(Role.ROLE_USER)
            .phoneNumber("+380*********")
            .email("test@gmail.com")
            .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
            .id(1L)
            .firstName("Test")
            .lastName("Test")
            .profilePicturePath("test")
            .role(Role.ROLE_USER)
            .phoneNumber("+380*********")
            .email("test@gmail.com")
            .build();
    }
}
