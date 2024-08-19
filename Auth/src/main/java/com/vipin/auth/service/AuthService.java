package com.vipin.auth.service;

import com.vipin.auth.exceptions.UserAuthenticationException;
import com.vipin.auth.model.dto.*;
import com.vipin.auth.model.response.LoginResponse;
import com.vipin.auth.model.response.WorkerResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserResponseDto registerUser(UserRequestDto user, HttpServletResponse response) throws Exception;


    WorkerResponseDto registerWorker(WorkerRequestDto workerRequestDto, HttpServletResponse response) throws Exception;

    Boolean emailExists(String email);

    String authenticateUser(LoginRequestDto loginRequestDto) throws UserAuthenticationException;



    LoginResponse getUserDetailsWhenAuthentication(String email);

    void setCookieInResponse(HttpServletResponse response,String token);

    boolean blockUser(String email);
    boolean unBlockUser(String email);

    void changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request);

    String googleAuthentication(UserRequestDto userRequestDto,HttpServletResponse response) throws Exception;
}
