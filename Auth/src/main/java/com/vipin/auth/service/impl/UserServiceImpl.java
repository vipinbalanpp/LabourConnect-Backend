package com.vipin.auth.service.impl;
import com.vipin.auth.client.UserServiceClient;
import com.vipin.auth.enums.Roles;
import com.vipin.auth.exceptions.InvalidEmailException;
import com.vipin.auth.exceptions.UserAuthenticationException;
import com.vipin.auth.exceptions.UserBlockedException;
import com.vipin.auth.model.dto.*;
import com.vipin.auth.model.entity.User;
import com.vipin.auth.model.response.LoginResponse;
import com.vipin.auth.repository.UserRepository;
import com.vipin.auth.service.UserService;
import com.vipin.auth.service.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper  modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    @Override
    public UserResponseDto registerUser(UserRequestDto userDto,HttpServletResponse response) throws Exception {
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new InvalidEmailException("Email id already exists");
        }
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userDto.getRole().equals(Roles.USER)){
            user.setRole(Roles.USER);
        }else if(userDto.getRole().equals(Roles.ADMIN)){
            user.setRole(Roles.ADMIN);
        }
        UserCreationRequest userCreationRequest = modelMapper.map(userDto, UserCreationRequest.class);
       UserResponseDto userResponseDto =  userServiceClient.createUser(userCreationRequest);
        log.info("Response when creating user from user service------: "+userResponseDto);
        User registeredUser = userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getEmail(),userDto.getPassword()));
        String token = jwtService.generateToken(authentication);
        setCookieInResponse(response,token);
       return userResponseDto;
    }

    @Override
    public WorkerResponse  registerWorker(WorkerRequestDto workerRequestDto, HttpServletResponse response) throws Exception {
        if(userRepository.existsByEmail(workerRequestDto.getEmail())){
            throw new InvalidEmailException("Mail Id exists");
        }
        User user = new User();
        user.setFullName(workerRequestDto.getFullname());
        user.setEmail(workerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(workerRequestDto.getPassword()));
        user.setRole(Roles.WORKER);
        User savedUser = userRepository.save(user);
        WorkerCreationRequest workerCreationRequest = modelMapper.map(workerRequestDto,WorkerCreationRequest.class);
        System.out.println(workerCreationRequest.getHouseName()+"------------->from service");
        WorkerResponse workerResponse = userServiceClient.createWorker(workerCreationRequest);
        log.info("Response when creating worker from user service------: "+workerResponse);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        workerRequestDto.getEmail(),workerRequestDto.getPassword()));
        String token = jwtService.generateToken(authentication);
        setCookieInResponse(response,token);
        return workerResponse;
    }



    @Override
    public Boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String authenticateUser(LoginRequestDto loginRequestDto) {
       if(!userRepository.existsByEmail(loginRequestDto.getEmail())){
           throw new UsernameNotFoundException("User does not exists");
       }
       if(userRepository.findByEmail(loginRequestDto.getEmail()).isBlocked()){
           throw  new UserBlockedException("Blocked By the Admin");
       }
        try{
            Authentication authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword()));
            return jwtService.generateToken(authentication);
        }catch (AuthenticationException e){
            throw  new UserAuthenticationException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LoginResponse getUserDetailsWhenAuthentication(String email) {
        User user = userRepository.findByEmail(email);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(user.getFullName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setRole(user.getRole());
        return loginResponse;
    }

    @Override
    public void setCookieInResponse(HttpServletResponse response,String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
            response.addCookie(cookie);
    }

    @Override
    public boolean blockUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setBlocked(true);
        userRepository.save(user);
        return true;
    }
    @Override
    public boolean unBlockUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setBlocked(false);
        userRepository.save(user);
        return true;
    }
}
