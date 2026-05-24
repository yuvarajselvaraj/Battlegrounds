package com.turf.battlegrounds.user;

public class UserMapper {

    public static UserResponseDto toDto(
            User user
    ) {

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
