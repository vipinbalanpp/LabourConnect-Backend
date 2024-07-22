package com.example.user.model.entity;

import com.example.user.model.dto.AddressDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String houseName;
    private String street;
    private String city;
    private String state;
    private String pincode;

    public Address(String houseName, String street, String city, String state, String pincode) {
        this.houseName = houseName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public Address(AddressDto addressDto) {
        this.houseName = addressDto.getHouseName();
        this.street = addressDto.getStreet();
        this.city = addressDto.getCity();
        this.state = addressDto.getState();
        this.pincode = addressDto.getPincode();
    }
}
