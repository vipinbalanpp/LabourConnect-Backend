package com.vipin.auth.model.response;

import com.vipin.auth.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String name;
    private String email;
    private Roles role;
    private String profileImageUrl;

}
