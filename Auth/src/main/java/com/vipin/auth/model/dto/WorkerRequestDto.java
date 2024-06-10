package com.vipin.auth.model.dto;

import com.vipin.auth.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerRequestDto {
    private String fullname;
    private String email;
    private Roles role;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String profileImageUrl;
    private String gender;
    private String password;
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
