package com.authservice.authservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class User extends org.springframework.security.core.userdetails.User {

    private String id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    public User(String id, String firstName, String lastName, String userName, String password) {
        super(userName, password, new ArrayList<>());
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }

    @Builder(builderMethodName = "loggedInUserIdBuilder")
    public static String getLoggedInUserId(String loggedInUserId) {
        return loggedInUserId;
    }
}

