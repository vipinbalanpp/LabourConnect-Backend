package com.example.user.controller;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.request.UserRequest;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.request.WorkerRequest;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.entity.User;
import com.example.user.service.ServicesService;
import com.example.user.service.UserService;
import com.example.user.service.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final ServicesService servicesService;
    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){

        return new ResponseEntity<>( userService.createUser(userRequest), HttpStatus.CREATED);
    }
    @PostMapping("/createWorker")
    public ResponseEntity<WorkerResponse> createWorker(@RequestBody WorkerRequest workerRequest){
        System.out.println(workerRequest.getFullname()+"---------------"+workerRequest.getEmail());
       WorkerResponse workerResponse =  userService.createWorker(workerRequest);
        return new ResponseEntity<>(workerResponse, HttpStatus.CREATED);
    }
    @GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request){
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        String role =jwtService.getRoleFromRequest(request);
        User user = userService.getUserByEmail(email);
        if(role.equals("USER") || role.equals("ADMIN")){
            return new ResponseEntity<>( userService.getUserDetails(email),HttpStatus.OK);
        }else if(role.equals("WORKER")){
            return new ResponseEntity<>( userService.getWorkerDetails(email),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers (){
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @GetMapping("/getAllWorkers")
    public ResponseEntity<List<WorkerResponse>> getAllWorkers (){
        List<WorkerResponse> users = userService.getAllWorkers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PatchMapping("/blockUser")
    public ResponseEntity<String> blockUser(@RequestParam String email){
        userService.blockUser(email);
        return  new ResponseEntity<>("User blocked successfully",HttpStatus.OK);
    }
    @PatchMapping("/unBlockUser")
    public ResponseEntity<String> unBlockUser(@RequestParam String email){
        userService.unBlockUser(email);
        return  new ResponseEntity<>("User unblocked successfully",HttpStatus.OK);
    }
    @PatchMapping("/blockWorker")
    public ResponseEntity<String> blockWorker(@RequestParam String email){
        userService.blockWorker(email);
        return  new ResponseEntity<>("Worker blocked successfully",HttpStatus.OK);
    }
    @PatchMapping("/unBlockWorker")
    public ResponseEntity<String> unBlockWorker(@RequestParam String email){
        userService.unBlockWorker(email);
        return  new ResponseEntity<>("Worker unblocked successfully",HttpStatus.OK);
    }
    @GetMapping("/get-services")
    public ResponseEntity<List<ServiceDto>> getAllServices(){
        List<ServiceDto> services = servicesService.getAllServices();
        return new ResponseEntity<>(services,HttpStatus.OK);
    }
    @PostMapping("/create-service")
    public ResponseEntity<ServiceDto> createService (@RequestBody ServiceDto serviceDto){
           ServiceDto service =  servicesService.createService(serviceDto);
        return new ResponseEntity<ServiceDto>(service,HttpStatus.CREATED);
    }
    @PostMapping("/add-address")
    public ResponseEntity<UserResponse> addAddress (@RequestBody AddressDto addressDto,
                                                    HttpServletRequest request){
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        UserResponse userResponse =     userService.addAddress(addressDto,email);
        return new ResponseEntity(userResponse,HttpStatus.CREATED);
    }

}
