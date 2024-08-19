package com.vipin.auth.model.entity;

import com.vipin.auth.enums.Roles;
import com.vipin.auth.model.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Roles role;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    public User(){}
    public User(UserRequestDto userRequestDto){
        this.fullName = userRequestDto.getFullName();
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto .getEmail();
    }
}
