package com.vipin.auth.model.dto;

import com.vipin.auth.enums.Roles;
import com.vipin.auth.model.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String fullName;
    private String username;
    private String email;
    private String profileImageUrl;
    private Roles role;
    private AddressDto address;

}
