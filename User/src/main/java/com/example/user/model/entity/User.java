package com.example.user.model.entity;


import com.example.user.model.dto.request.UserRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userinfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String profileImageUrl;
    private Roles role;
    private String mobileNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;
    private LocalDateTime createdAt;
    private boolean isBlocked;
    public User(UserRequestDto userResponseDto){
        this.fullName = userResponseDto.getFullName();
        this.email = userResponseDto.getEmail();
        this.role = Roles.USER;
        this.createdAt = LocalDateTime.now();
    }
}
