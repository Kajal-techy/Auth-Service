package com.authservice.authservice.dao;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;
}

