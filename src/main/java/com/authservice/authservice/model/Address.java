package com.authservice.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {

    private int houseNumber;

    private int streetNo;

    private String streetName;

    private String city;

    private String state;

    private String country;

    private int pincode;
}
