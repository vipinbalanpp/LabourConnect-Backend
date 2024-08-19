package com.vipin.auth.model.response;

import com.vipin.auth.enums.Roles;
import com.vipin.auth.model.dto.AddressDto;
import com.vipin.auth.model.dto.ServiceDto;
import com.vipin.auth.model.dto.WorkDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerResponseDto {
    private String fullName;
    private String username;
    private String email;
    private ServiceDto service;
    private Long experience;
    private String mobileNumber;
    private ServiceDto serviceDto;
    private double serviceCharge;
    private String about;
    private String gender;
    private LocalDate dateOfBirth;
    private Roles role;
    private String profileImageUrl;
    private AddressDto address;
    private List<WorkDto> works;
}