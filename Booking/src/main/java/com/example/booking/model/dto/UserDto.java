package com.example.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String fullName;
    private String email;
    private String profileImageUrl;
    private String role;
    private String mobileNumber;
    private AddressDto address;
    private LocalDateTime createdAt;
    private boolean isBlocked;
}
