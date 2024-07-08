package com.example.user.controller;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponseDto;
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
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequest){
    try {
        System.out.println("fullname  :" + userRequest.getFullName()+"          Email:" + userRequest.getEmail());
        return new ResponseEntity<>( userService.createUser(userRequest), HttpStatus.CREATED);

    } catch (RuntimeException e){
        throw  new RuntimeException("Something went wrong");
    }
    }
    @PostMapping("/createWorker")
    public ResponseEntity<WorkerResponseDto> createWorker(@RequestBody WorkerRequestDto workerRequest){

      try {
          WorkerResponseDto workerResponse =  userService.createWorker(workerRequest);
          return new ResponseEntity<>(workerResponse, HttpStatus.CREATED);
      }catch (RuntimeException e){
          throw  new RuntimeException("Something went wrong");
      }
    }
    @GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request){
        try {

        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        String role =jwtService.getRoleFromRequest(request);
        if(role.equals("USER") || role.equals("ADMIN")){
            return new ResponseEntity<>( userService.getUserDetails(email),HttpStatus.OK);
        }else if(role.equals("WORKER")){
            return new ResponseEntity<>( userService.getWorkerDetails(email),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @GetMapping("/getUserDetails")
    public ResponseEntity<UserResponseDto> getUserDetailsById(@RequestParam Long id){
        UserResponseDto userResponseDto = userService.getUserDetailsById(id);
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }
    @GetMapping("/workerDetails")
    public ResponseEntity<WorkerResponseDto> getWorkerDetailsById(@RequestParam Long id){
        WorkerResponseDto workerResponseDto = userService.getWorkerDetailsById(id);
        return new ResponseEntity<>(workerResponseDto,HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> getAllUsers (@RequestParam int pageNumber,
                                                              @RequestParam(required = false) String searchInput,
                                                              @RequestParam(required = false)Boolean isBlocked){
        try {
            System.out.println("In get All users");
            List<UserResponseDto> users = userService.getAllUsers(pageNumber,searchInput,isBlocked);
            return new ResponseEntity<>(users,HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @GetMapping("/getTotalPageNumbersOfUsers")
    public ResponseEntity<Integer> getTotalPageNumbersOfUsers(
            @RequestParam(required = false) String searchInput,
            @RequestParam(required = false) Boolean isBlocked) {
        try {
            int totalPages = userService.getTotalPageNumbersOfUsers(searchInput, isBlocked);
            return new ResponseEntity<>(totalPages, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }
    @GetMapping("/getAllWorkers")
    public ResponseEntity<List<WorkerResponseDto>> getAllWorkers (@RequestParam Integer pageNumber,
                                                                  @RequestParam(required = false) Integer pageSize,
                                                                  @RequestParam(required = false) Long serviceId,
                                                                  @RequestParam(required = false) String searchInput,
                                                                  @RequestParam(required = false) Boolean isBlocked){
        try {
            List<WorkerResponseDto> users = userService.getAllWorkers(pageNumber,searchInput,isBlocked,serviceId,pageSize);
            return new ResponseEntity<>(users,HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @GetMapping("/getTotalPageNumbersOfWorkers")
    public ResponseEntity<Integer> getTotalPageNumbersOfWorkers(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchInput,
            @RequestParam(required = false) Boolean isBlocked) {
        try {
            int totalPages = userService.getTotalPageNumbersOfWorkers(serviceId,searchInput, isBlocked,pageSize);
            return new ResponseEntity<>(totalPages, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }
    @PatchMapping("/blockUser")
    public ResponseEntity<String> blockUser(@RequestParam String email){
        try {
            userService.blockUser(email);
            return  new ResponseEntity<>("User blocked successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }

    }
    @PatchMapping("/unBlockUser")
    public ResponseEntity<String> unBlockUser(@RequestParam String email){
        try {
            userService.unBlockUser(email);
            return  new ResponseEntity<>("User unblocked successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }

    }
    @PatchMapping("/blockWorker")
    public ResponseEntity<String> blockWorker(@RequestParam String email){
        try {
            userService.blockWorker(email);
            return  new ResponseEntity<>("Worker blocked successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @PatchMapping("/unBlockWorker")
    public ResponseEntity<String> unBlockWorker(@RequestParam String email){
        try {
            userService.unBlockWorker(email);
            return  new ResponseEntity<>("Worker unblocked successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }

    }


    @PutMapping("/edit-worker")
    public ResponseEntity<String> editWorker (@RequestBody EditWorkerRequestDto editWorkerDto,
                                                    HttpServletRequest request){
        try {
            String token = jwtService.getTokenFromRequest(request);
            String email = jwtService.getEmailFromToken(token);
            userService.editWorkerDetails(editWorkerDto,email);
            return new ResponseEntity("worker details edited successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @PutMapping("/edit-profileImage")
    public ResponseEntity<String> editWorker (@RequestParam String profileImageUrl,
                                              HttpServletRequest request){
        try {
            String token = jwtService.getTokenFromRequest(request);
            String email = jwtService.getEmailFromToken(token);
            String role =jwtService.getRoleFromRequest(request);
            userService.changeProfileImageUrl(role,email,profileImageUrl);
            System.out.println(profileImageUrl);
            return new ResponseEntity("Profile image updated successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw new RuntimeException("something went wrong while updating image");
        }
    }
    @PostMapping("/add-address")
    public ResponseEntity<String> addAddress (@RequestBody AddressDto addressDto ,
                                               HttpServletRequest request){
        try {
            userService.addUserAddress(addressDto,request);
            return new ResponseEntity<>("Address added successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong while editing profile");
        }
    }
    @PostMapping("/edit-address")
    public ResponseEntity<String> editProfile (@RequestBody AddressDto addressDto ,
                                               HttpServletRequest request){
       try {
           userService.editAddress(addressDto,request);
           return new ResponseEntity<>("Address edited successfully",HttpStatus.OK);
       }catch (RuntimeException e){
           throw  new RuntimeException("Something went wrong while editing profile");
       }
    }
    @PutMapping("/edit-fullName")
    public ResponseEntity<String> editFullName(@RequestParam String fullName,
                                               HttpServletRequest request){
        try {
            userService.editFullName(fullName,request);
            return new ResponseEntity<>("FullName updated successfully",HttpStatus.OK);
        }catch (RuntimeException e){
            throw new RuntimeException("Something went wrong while changing fullname");
        }
    }

}
