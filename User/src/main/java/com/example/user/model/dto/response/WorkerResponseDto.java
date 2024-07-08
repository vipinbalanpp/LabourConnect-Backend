package com.example.user.model.dto.response;

import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.entity.Address;
import com.example.user.model.entity.Roles;
import com.example.user.model.entity.Works;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private ServiceDto service;
    private Long experience;
    private String mobileNumber;
    private double serviceCharge;
    private String about;
    private String gender;
    private LocalDate dateOfBirth;
    private Roles role;
    private String profileImageUrl;
    private boolean isBlocked;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private AddressDto address;
    private List<Works> works;
}
