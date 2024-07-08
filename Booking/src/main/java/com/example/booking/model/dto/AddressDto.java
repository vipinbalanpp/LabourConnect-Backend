package com.example.booking.model.dto;

import com.example.booking.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    public AddressDto (Address address){
        this.houseName = address.getHouseName();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.pincode = address.getPincode();
    }
    private String houseName;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
