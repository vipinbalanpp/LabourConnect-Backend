package com.vipin.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String houseName;
    private String street;
    private String city;
    private String state;
    private String pinCode;
}
