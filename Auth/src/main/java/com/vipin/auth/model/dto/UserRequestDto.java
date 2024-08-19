package com.vipin.auth.model.dto;

import com.vipin.auth.enums.Roles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private Roles role;
    private String fullName;
    private String username;
    private String password;
    private String email;

}
