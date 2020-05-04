package com.authservice.authservice.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

