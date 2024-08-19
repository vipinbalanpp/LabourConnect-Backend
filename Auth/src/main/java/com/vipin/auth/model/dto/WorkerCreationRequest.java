package com.vipin.auth.model.dto;

import com.vipin.auth.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCreationRequest {
    private String fullName;
    private String username;
    private String email;
    private Roles role;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String profileImageUrl;
    private String gender;
    private Long serviceId;
    private Long experience;
    private Double serviceCharge;
    private String houseName;
    private String street;
    private String city;
    private String state;
    private String pinCode;
    private String about;
    public WorkerCreationRequest(WorkerRequestDto workerRequestDto){
        this.fullName = workerRequestDto.getFullName();
        this.username = workerRequestDto.getUsername();
        this.email = workerRequestDto.getEmail();
        this.role = workerRequestDto.getRole();
        this.mobileNumber = workerRequestDto.getMobileNumber();
        this.dateOfBirth = workerRequestDto.getDateOfBirth();
        this.profileImageUrl = workerRequestDto.getProfileImageUrl();
        this.gender = workerRequestDto.getGender();
        this.serviceId = workerRequestDto.getServiceId();
        this.experience = workerRequestDto.getExperience();
        this.serviceCharge = workerRequestDto.getServiceCharge();
        this.houseName = workerRequestDto.getHouseName();
        this.street = workerRequestDto.getStreet();
        this.city = workerRequestDto.getCity();
        this.state = workerRequestDto.getState();
        this.pinCode = workerRequestDto.getPinCode();
        this.about = workerRequestDto.getAbout();
    }
}
