package com.vipin.auth.model.entity;

import com.vipin.auth.enums.Roles;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
@Getter
@Setter
@Table(name = "userinfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String fullName;
    private String password;
    private String email;
    private Roles role;
    private boolean isBlocked;

}
