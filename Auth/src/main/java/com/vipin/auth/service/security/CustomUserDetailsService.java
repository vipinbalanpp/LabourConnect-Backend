package com.vipin.auth.service.security;

import com.vipin.auth.model.entity.User;
import com.vipin.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(!userRepository.existsByEmail(email)){
            throw new UsernameNotFoundException("Username does not exists !");
        }
        User user  = userRepository.findByEmail(email);
        return new CustomUserDetails(user);
    }
}
