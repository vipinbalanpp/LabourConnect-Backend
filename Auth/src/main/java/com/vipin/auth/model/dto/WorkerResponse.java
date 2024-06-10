package com.vipin.auth.model.dto;

import com.vipin.auth.enums.Roles;
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
public class WorkerResponse {
    private String fullName;
    private String email;
    private String expertiseIn;
    private Long experience;
    private String mobileNumber;
    private double serviceCharge;
    private String about;
    private String gender;
    private LocalDate dateOfBirth;
    private Roles role;
    private String profileImageUrl;
    private AddressDto address;
    private List<WorkDto> works;
}