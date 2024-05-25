package com.vipin.auth.service.impl;

import com.vipin.auth.enums.Roles;
import com.vipin.auth.model.dto.UserRequestDto;
import com.vipin.auth.model.entity.User;
import com.vipin.auth.repository.UserRepository;
import com.vipin.auth.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private  UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    UserServiceImpl (UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void registerUser(UserRequestDto userDto) {
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userDto.getRole().equals(Roles.USER)){
            user.setRole(Roles.USER);
        }else if(userDto.getRole().equals(Roles.ADMIN)){
            user.setRole(Roles.ADMIN);
        }
        userRepository.save(user);
    }
}
