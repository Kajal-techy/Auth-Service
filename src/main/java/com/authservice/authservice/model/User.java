package com.authservice.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private String id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private Address address;
}

