package com.example.user.model.dto.response;

import com.example.user.model.dto.AddressDto;
import com.example.user.model.entity.Address;
import com.example.user.model.entity.Roles;
import com.example.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String profileImageUrl;
    private Roles role;
    private String mobileNumber;
    private AddressDto address;
    private LocalDateTime createdAt;
    private boolean isBlocked;
    public UserResponseDto (User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
        this.role = user.getRole();
        this.mobileNumber = user.getMobileNumber();
        this.createdAt = user.getCreatedAt();
        this.isBlocked = user.isBlocked();
        if(user.getAddress() != null) this.address = new AddressDto(user.getAddress());
    }
}
