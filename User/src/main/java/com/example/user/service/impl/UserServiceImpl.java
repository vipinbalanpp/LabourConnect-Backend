package com.example.user.service.impl;
import com.example.user.client.AuthServiceClient;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.UserRequest;
import com.example.user.model.dto.request.WorkerRequest;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.entity.Address;
import com.example.user.model.entity.Roles;
import com.example.user.model.entity.User;
import com.example.user.model.entity.Worker;
import com.example.user.repository.AddressRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WorkerRepository;
import com.example.user.service.UserService;
import com.example.user.service.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final WorkerRepository workerRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthServiceClient authServiceClient;
    @Override
    public UserResponse createUser(UserRequest userRequest) {
       User user = new User();
       user.setFullName(userRequest.getFullName());
       user.setEmail(userRequest.getEmail());
       user.setRole(Roles.USER);
       user.setCreatedAt(LocalDateTime.now());
       UserResponse userResponse = modelMapper.map(userRepository.save(user),UserResponse.class);
       return userResponse;
    }

    @Override
    public WorkerResponse createWorker(WorkerRequest workerRequest) {
        log.info("worker request dto: "+ workerRequest);
        Address address = new Address();
        address.setHouseName(workerRequest.getHouseName());
        System.out.println("housename--------------->"+workerRequest.getHouseName());
        address.setStreet(workerRequest.getStreet());
        address.setCity(workerRequest.getCity());
        address.setState(workerRequest.getState());
        address.setPincode(workerRequest.getPincode());
        Worker worker = new Worker();
        worker.setAddress(addressRepository.save(address));
        worker.setFullName(workerRequest.getFullname());
        worker.setEmail(workerRequest.getEmail());
        worker.setRole(Roles.WORKER);
        worker.setMobileNumber(workerRequest.getMobileNumber());
        worker.setDateOfBirth(workerRequest.getDateOfBirth());
        worker.setProfileImageUrl(workerRequest.getProfileImageUrl());
        worker.setGender(workerRequest.getGender());
        worker.setExpertiseIn(workerRequest.getExpertiseIn());
        worker.setExperience(workerRequest.getExperience());
        worker.setServiceCharge(workerRequest.getServiceCharge());
        worker.setAbout(workerRequest.getAbout());
        worker.setCreatedAt(LocalDateTime.now());
        worker.setCreatedAt(LocalDateTime.now());
        log.info("worker: "+worker);
        Worker savedWorker = workerRepository.save(worker);
        AddressDto addressDto = modelMapper.map(savedWorker.getAddress(),AddressDto.class);
       WorkerResponse workerResponse = modelMapper.map(savedWorker,WorkerResponse.class);
       workerResponse.setAddress( addressDto);
       return workerResponse;

    }

    @Override
    public UserResponse getUserDetails(String email) {
         User user= userRepository.findByEmail(email);
         if(user == null){
             return null;
         }
        System.out.println(user.getAddress()+"--------------------> address");
         return modelMapper.map(user,UserResponse.class);

    }

    @Override
    public WorkerResponse getWorkerDetails(String email) {
        System.out.println(email+"-------------------->emailid from this");
        Worker worker= workerRepository.findByEmail(email);
        System.out.println(email);
        System.out.println(worker.getFullName());
        return modelMapper.map(worker,WorkerResponse.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponse> getAllUsers() {
       List<User>users =  userRepository.findByRole(Roles.USER);
       List<UserResponse> userResponses= new ArrayList<>();
       for(User user: users){
           UserResponse userResponse = new UserResponse();
           userResponse.setFullName(user.getFullName());
           userResponse.setEmail(user.getEmail());
           userResponse.setRole(user.getRole());
           userResponse.setProfileImageUrl(user.getProfileImageUrl());
           userResponse.setMobileNumber(user.getMobileNumber());
           if(user.getAddress() != null) userResponse.setAddress(modelMapper.map(user.getAddress(),AddressDto.class));
           else userResponse.setAddress(null);
           userResponse.setBlocked(user.isBlocked());
           userResponse.setCreatedAt(user.getCreatedAt());
           userResponses.add(userResponse);
       }
       return  userResponses;
    }

    @Override
    public void blockUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setBlocked(true);
        boolean response = authServiceClient.blockUser(email);
        log.info("Response from auth service when blocking--->" +response);
        userRepository.save(user);
    }

    @Override
    public void unBlockUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setBlocked(false);
        boolean response = authServiceClient.unBlockUser(email);
        userRepository.save(user);
    }

    @Override
    public List<WorkerResponse> getAllWorkers() {

        List<Worker> workers = workerRepository.findAll();
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for(Worker worker : workers){
            WorkerResponse workerResponse = new WorkerResponse();
            workerResponse.setFullName(worker.getFullName());
            workerResponse.setEmail(worker.getEmail());
            workerResponse.setExpertiseIn(worker.getExpertiseIn());
            workerResponse.setExperience(worker.getExperience());
            workerResponse.setMobileNumber(worker.getMobileNumber());
            workerResponse.setServiceCharge(worker.getServiceCharge());
            workerResponse.setAbout(worker.getAbout());
            workerResponse.setGender(worker.getGender());
            workerResponse.setDateOfBirth(worker.getDateOfBirth());
            workerResponse.setBlocked(worker.isBlocked());
            workerResponse.setVerified(worker.isVerified());
            workerResponse.setCreatedAt(worker.getCreatedAt());
            workerResponse.setRole(worker.getRole());
            workerResponse.setProfileImageUrl(worker.getProfileImageUrl());
            AddressDto addressDto = modelMapper.map(worker.getAddress(),AddressDto.class);
            workerResponse.setAddress(addressDto);
            workerResponse.setWorks(worker.getWorks());
            workerResponses.add(workerResponse);

        }return workerResponses;
    }

    @Override
    public void blockWorker(String email) {
        Worker worker = workerRepository.findByEmail(email);
        worker.setBlocked(true);
        boolean response = authServiceClient.blockUser(email);
        workerRepository.save(worker);
    }

    @Override
    public void unBlockWorker(String email) {
        Worker worker = workerRepository.findByEmail(email);
        worker.setBlocked(false);
        boolean response = authServiceClient.unBlockUser(email);
        workerRepository.save(worker);
    }

    @Override
    public UserResponse addAddress(AddressDto addressDto, String email) {
        Address address = new Address();
        address.setHouseName(addressDto.getHouseName());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPincode(addressDto.getPincode());
        address = addressRepository.save(address);
        User user = userRepository.findByEmail(email);
        user.setAddress(address);
        userRepository.save(user);
        return modelMapper.map(user,UserResponse.class);
    }


}
