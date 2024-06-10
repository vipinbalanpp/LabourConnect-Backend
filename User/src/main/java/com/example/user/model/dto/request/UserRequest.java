package com.example.user.model.dto.request;

import com.example.user.model.entity.Address;
import com.example.user.model.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String fullName;
    private String email;
    private Roles role;

}
