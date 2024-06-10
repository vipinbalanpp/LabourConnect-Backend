package com.example.user.model.dto.request;

import com.example.user.model.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerRequest {
    private String fullname;
    private String email;
    private Roles role;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String profileImageUrl;
    private String gender;
    private String expertiseIn;
    private Long experience;
    private Double serviceCharge;
    private String houseName;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String about;
}
