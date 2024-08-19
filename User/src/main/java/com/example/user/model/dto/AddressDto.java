package com.example.user.model.dto;

import com.example.user.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String houseName;
    private String street;
    private String city;
    private String state;
    private Long pinCode;
    public AddressDto(Address address){
        this.houseName = address.getHouseName();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.pinCode = address.getPinCode();
    }
}
