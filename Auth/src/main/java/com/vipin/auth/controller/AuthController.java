package com.vipin.auth.controller;
import com.vipin.auth.model.dto.UserRequestDto;
import com.vipin.auth.model.dto.UserResponseDto;
import com.vipin.auth.model.entity.User;
import com.vipin.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth/api/v1")
public class AuthController {

    private UserService userService;
    private RestTemplate restTemplate;

    public AuthController(UserService userService, RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        this.userService = userService;
    }


    @PostMapping("/user/register")
    public ResponseEntity<UserResponseDto> registerUser (@RequestBody UserRequestDto user){
       try {
           System.out.println(user);
           userService.registerUser(user);
       }catch (Exception e){
           throw new RuntimeException("Something went wrong");
       }
       return new ResponseEntity<>(new UserResponseDto(),HttpStatus.CREATED);
    }

//    @PostMapping("/worker/register")
//    public ResponseEntity<UserResponseDto> registerWorker (@RequestBody UserRequestDto user){
//        try {
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }
//        return new ResponseEntity<>(new UserResponseDto(),HttpStatus.CREATED);
//    }

    @GetMapping("/hello")
    public String hollow(){
        return "hello";
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody User user) {
        return new ResponseEntity<>(new UserResponseDto(true,"user logged in ","success"),HttpStatus.OK);
    }
}
