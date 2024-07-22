package com.vipin.auth.controller;
import com.vipin.auth.model.dto.*;
import com.vipin.auth.model.response.LoginResponse;
import com.vipin.auth.model.response.UserResponse;
import com.vipin.auth.model.response.WorkerResponseDto;
import com.vipin.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth/api/v1")
@Slf4j
public class AuthController {

    private UserService userService;
    private RestTemplate restTemplate;

    public AuthController(UserService userService, RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        this.userService = userService;
    }
    @PostMapping("/googleAuth")
    public ResponseEntity<?> googleAuthentication(@RequestBody UserRequestDto userRequestDto, HttpServletResponse servletResponse) {
        try {
            String response = userService.googleAuthentication(userRequestDto, servletResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Google authentication failed for user: {}", userRequestDto.getEmail(), e);
            throw new RuntimeException("Google authentication failed: " + e.getMessage(), e);
        }
    }


    @PostMapping("/user/register")
    public ResponseEntity<UserResponseDto> registerUser (@RequestBody UserRequestDto user,
                                                         HttpServletResponse response){
        UserResponseDto userResponseDto = null;
        try {
            userResponseDto= userService.registerUser(user,response);
        }catch (Exception e){
            return  new ResponseEntity<>(userResponseDto,HttpStatus.FORBIDDEN);
        }

       return new ResponseEntity<>(userResponseDto,HttpStatus.CREATED);
    }
    @GetMapping("/emailExists/{email}")
    public Boolean emailExists(@PathVariable String email){
        return userService.emailExists(email);
    }

    @PostMapping("/worker/register")
    public ResponseEntity<WorkerResponseDto> registerWorker (@RequestBody WorkerRequestDto workerRequestDto, HttpServletResponse response) throws Exception {
//        try {
            WorkerResponseDto  workerResponse = userService.registerWorker(workerRequestDto,response);
            return new ResponseEntity<>(workerResponse,HttpStatus.CREATED);
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
            String token = userService.authenticateUser(loginRequestDto);
            userService.setCookieInResponse(response, token);
            LoginResponse loginResponse = userService.getUserDetailsWhenAuthentication(loginRequestDto.getEmail());
            return new ResponseEntity<>(new UserResponse(true, loginResponse, "success"), HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "jwt", required = false) String authToken, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        if (authToken != null) {
            Cookie cookie = new Cookie("jwt", null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return ResponseEntity.ok("Logout successful");
    }
    @PutMapping("/blockUser")
    public boolean blockUser (@RequestParam String email){
        return userService.blockUser(email);
    }
    @PutMapping("/unBlockUser")
    public boolean unBlockUser (@RequestParam String email){
        return userService.unBlockUser(email);
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                                 HttpServletRequest request){
        userService.changePassword(changePasswordDto,request);
        return new ResponseEntity<>("Password changed Successfully",HttpStatus.OK);
    }

}
