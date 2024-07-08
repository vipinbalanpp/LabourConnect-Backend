package com.example.user.service.impl;
import com.example.user.client.AuthServiceClient;
import com.example.user.excepation.UserNotFoundException;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.*;
import com.example.user.repository.AddressRepository;
import com.example.user.repository.ServicesRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WorkerRepository;
import com.example.user.service.UserService;
import com.example.user.service.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ServicesRepository servicesRepository;
    @Override
    public UserResponseDto createUser(UserRequestDto userRequest) {
       User user = new User();
       log.info("user to be created --------->:" + userRequest.getFullName()+" -----------> " + userRequest.getEmail());
       user.setFullName(userRequest.getFullName());
       user.setEmail(userRequest.getEmail());
       user.setRole(Roles.USER);
       user.setCreatedAt(LocalDateTime.now());
       UserResponseDto userResponse = modelMapper.map(userRepository.save(user),UserResponseDto.class);
       return userResponse;
    }

    @Override
    public WorkerResponseDto createWorker(WorkerRequestDto workerRequest) {
        log.info("worker request dto: "+ workerRequest);
        Address address = new Address();
        address.setHouseName(workerRequest.getHouseName());
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
        Services service = servicesRepository.findByServiceName(workerRequest.getExpertiseIn());
        worker.setService(service);
        worker.setExperience(workerRequest.getExperience());
        worker.setServiceCharge(workerRequest.getServiceCharge());
        worker.setAbout(workerRequest.getAbout());
        worker.setCreatedAt(LocalDateTime.now());
        worker.setCreatedAt(LocalDateTime.now());
        log.info("worker: "+worker);
        Worker savedWorker = workerRepository.save(worker);
        log.info("worker saves: " + savedWorker);
        AddressDto addressDto = modelMapper.map(savedWorker.getAddress(),AddressDto.class);
       WorkerResponseDto workerResponse = modelMapper.map(savedWorker,WorkerResponseDto.class);
       workerResponse.setAddress( addressDto);
       return workerResponse;

    }

    @Override
    public UserResponseDto getUserDetails(String email) {
         User user= userRepository.findByEmail(email);
         return modelMapper.map(user,UserResponseDto.class);

    }

    @Override
    public WorkerResponseDto getWorkerDetails(String email) {
        Worker worker= workerRepository.findByEmail(email);
        return modelMapper.map(worker,WorkerResponseDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponseDto> getAllUsers(int pageNumber,String searchInput,Boolean isBlocked) {
        System.out.println(searchInput);
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> usersPage;
        if(searchInput != null && isBlocked !=null){
            usersPage = isBlocked ? userRepository.findByRoleAndIsBlockedBySearch(Roles.USER,searchInput,pageable):userRepository.findByRoleAndNotBlockedBySearch(Roles.USER, searchInput,pageable);
        }else if(searchInput == null && isBlocked != null){
            usersPage = isBlocked==true ? userRepository.findByRoleAndNotBlocked(Roles.USER,pageable) : userRepository.findByRoleAndIsBlocked(Roles.USER,pageable);
        }else if (searchInput != null && isBlocked == null){
            usersPage = userRepository.findByRoleAndSearchInput(Roles.USER,searchInput,pageable);
        }else {
            usersPage = userRepository.findByRole(Roles.USER, pageable);
        }
        return usersPage.stream().map(user -> {
            UserResponseDto userResponse = new UserResponseDto();
            userResponse.setFullName(user.getFullName());
            userResponse.setEmail(user.getEmail());
            userResponse.setRole(user.getRole());
            userResponse.setProfileImageUrl(user.getProfileImageUrl());
            userResponse.setMobileNumber(user.getMobileNumber());
            if (user.getAddress() != null)
                userResponse.setAddress(modelMapper.map(user.getAddress(), AddressDto.class));
            else
                userResponse.setAddress(null);
            userResponse.setBlocked(user.isBlocked());
            userResponse.setCreatedAt(user.getCreatedAt());
            return userResponse;
        }).collect(Collectors.toList());
    }
    @Override
    public Integer getTotalPageNumbersOfUsers(String searchInput, Boolean isBlocked) {
        long response;
        if (searchInput != null && isBlocked != null) {
            response = isBlocked ?
                    userRepository.countByRoleAndIsBlockedBySearch(Roles.USER, searchInput) :
                    userRepository.countByRoleAndNotBlockedBySearch(Roles.USER, searchInput);
        } else if (searchInput == null && isBlocked != null) {
            response = isBlocked ?
                    userRepository.countByRoleAndNotBlocked(Roles.USER) :
                    userRepository.countByRoleAndIsBlocked(Roles.USER);
        } else if (searchInput != null && isBlocked == null) {
            response = userRepository.countByRoleAndSearchInput(Roles.USER, searchInput);
        } else {
            response = userRepository.countByRole(Roles.USER);
        }

        int pageSize = 8;
        return (int) Math.ceil((double) response / pageSize);
    }

    @Override
    public Integer getTotalPageNumbersOfWorkers(Long serviceId, String searchInput, Boolean isBlocked,Integer pageSize) {
        Integer numberOfWorkers = workerRepository.countAllWorkers(serviceId,searchInput,isBlocked);
        if(pageSize == null){
            pageSize = 8;
        }
        return (int) Math.ceil((double) numberOfWorkers / pageSize);
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
    public List<WorkerResponseDto> getAllWorkers(Integer pageNumber,String searchInput,Boolean isBlocked,Long serviceId,Integer pageSize) {
        if(pageSize == null){
            pageSize=8;
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Worker> workers= workerRepository.findAllWorkers(serviceId,searchInput,isBlocked,pageable);
        List<WorkerResponseDto> workerResponses = new ArrayList<>();
        for(Worker worker : workers){
            WorkerResponseDto workerResponse = new WorkerResponseDto();
            workerResponse.setFullName(worker.getFullName());
            workerResponse.setEmail(worker.getEmail());
           workerResponse.setService(modelMapper.map(worker.getService(), ServiceDto.class));
            workerResponse.setExperience(worker.getExperience());
            workerResponse.setMobileNumber(worker.getMobileNumber());
            workerResponse.setId(worker.getId());
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
        }
        return workerResponses;
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
    public UserResponseDto addAddress(AddressDto addressDto, String email) {
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
        return modelMapper.map(user,UserResponseDto.class);
    }

    @Override
    public void editWorkerDetails(EditWorkerRequestDto editWorkerDto, String email) {
        Worker worker = workerRepository.findByEmail(email);
        worker.setFullName(editWorkerDto.getFullName());
        worker.setMobileNumber(editWorkerDto.getMobileNumber());
        worker.setDateOfBirth(LocalDate.parse(editWorkerDto.getDateOfBirth()));
        Services service = servicesRepository.findByServiceName(editWorkerDto.getExpertiseIn());
        worker.setService(service);
        worker.setExperience(editWorkerDto.getExperience());
        worker.setServiceCharge(editWorkerDto.getServiceCharge());
        worker.setAbout(editWorkerDto.getAbout());
        workerRepository.save(worker);


    }

    @Override
    public void changeProfileImageUrl(String role, String email, String profileImageUrl) {
        try {
            System.out.println(role);
            if(role.equals("USER")){
                User user = userRepository.findByEmail(email);
                user.setProfileImageUrl(profileImageUrl);
                userRepository.save(user);
            }else if(role.equals("WORKER")){
                System.out.println(role);
                Worker worker = workerRepository.findByEmail(email);
                worker.setProfileImageUrl(profileImageUrl);
                workerRepository.save(worker);
            }
        }catch (UserNotFoundException e){
            throw  new UserNotFoundException("Error occurred while updating image");
        }
    }

    @Override
    public void editAddress(AddressDto addressDto, HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        String role =jwtService.getRoleFromRequest(request);
        Address address = new Address();
        address.setHouseName(addressDto.getHouseName());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPincode(addressDto.getPincode());
        address = addressRepository.save(address);
        if(role.equals("USER")){
            User user = userRepository.findByEmail(email);
            user.setAddress(address);
            userRepository.save(user);
        }else if(role.equals("WORKER")){
            Worker worker = workerRepository.findByEmail(email);
            worker.setAddress(address);
            workerRepository.save(worker);
        }
    }

    @Override
    public void editFullName(String fullName, HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        user.setFullName(fullName);
        userRepository.save(user);
    }

    @Override
    public void addUserAddress(AddressDto addressDto, HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        Address address = new Address();
        address.setHouseName(addressDto.getHouseName());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPincode(addressDto.getPincode());
        address = addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);
    }



    @Override
    public UserResponseDto getUserDetailsById(Long id) {
        return modelMapper.map(userRepository.findById(id),UserResponseDto.class);
    }

    @Override
    public WorkerResponseDto getWorkerDetailsById(Long id) {
        return modelMapper.map(workerRepository.findById(id),WorkerResponseDto.class);
    }




}
