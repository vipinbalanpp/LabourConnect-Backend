package com.example.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditWorkerRequestDto {
    private String fullName;
    private String mobileNumber;
    private String dateOfBirth;
    private String expertiseIn;
    private Long experience;
    private Double serviceCharge;
    private String about;
}
