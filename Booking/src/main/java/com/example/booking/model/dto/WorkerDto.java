package com.example.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDto {
    private String fullName;
    private String email;
    private ServiceDto service;
    private Long experience;
    private String mobileNumber;
    private double serviceCharge;
    private String about;
    private String gender;
    private LocalDate dateOfBirth;
    private String role;
    private String profileImageUrl;
    private boolean isBlocked;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private AddressDto address;
}
