package com.example.user.model.dto.response;

import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.entity.Roles;
import com.example.user.model.entity.Worker;
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
    private String username;
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
    public WorkerResponseDto(Worker worker){
        this.fullName =worker.getFullName();
        this.username = worker.getUsername();
        this.email = worker.getEmail();
        this.experience = worker.getExperience();
        this.mobileNumber = worker.getMobileNumber();
        this.id = worker.getId();
        this.serviceCharge =  worker.getServiceCharge();
        this.about = worker.getAbout();
        this.gender = worker.getGender();
        this.dateOfBirth = worker.getDateOfBirth();
        this.isBlocked = worker.isBlocked();
        this.isVerified = worker.isVerified();
        this.createdAt = worker.getCreatedAt();
        this.role = worker.getRole();
        this.profileImageUrl = worker.getProfileImageUrl();
        this.address =  new AddressDto(worker.getAddress());
        if(worker.getWorks() != null)
        this.works = worker.getWorks();
    }
}
