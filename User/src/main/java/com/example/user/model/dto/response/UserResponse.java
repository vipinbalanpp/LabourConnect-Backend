package com.example.user.model.dto.response;

import com.example.user.model.dto.AddressDto;
import com.example.user.model.entity.Address;
import com.example.user.model.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String fullName;
    private String email;
    private String profileImageUrl;
    private Roles role;
    private String mobileNumber;
    private AddressDto address;
    private LocalDateTime createdAt;
    private boolean isBlocked;
}
