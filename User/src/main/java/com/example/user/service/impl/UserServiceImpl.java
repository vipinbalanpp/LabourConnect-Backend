package com.example.user.service.impl;
import com.example.user.client.AuthServiceClient;
import com.example.user.excepation.UserNotFoundException;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.*;
import com.example.user.repository.AddressRepository;
import com.example.user.repository.ServicesRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WorkerRepository;
import com.example.user.service.UserService;
import com.example.user.service.util.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
       user.setFullName(userRequest.getFullName());
       user.setEmail(userRequest.getEmail());
       user.setRole(Roles.USER);
       user.setCreatedAt(LocalDateTime.now());
       UserResponseDto userResponse = modelMapper.map(userRepository.save(user),UserResponseDto.class);
       return userResponse;
    }

    @Override
    @Transactional
    public WorkerResponseDto createWorker(WorkerRequestDto workerRequestDto) {
        Address address =  new Address(workerRequestDto.getHouseName(),workerRequestDto.getStreet(),workerRequestDto.getCity(),workerRequestDto.getState(),workerRequestDto.getPincode());
        addressRepository.save(address);
        log.info("address:{}",address);
        System.out.println(workerRequestDto.getServiceCharge());
        Worker worker = new Worker(workerRequestDto);
        worker.setAddress(address);
        if(!servicesRepository.existsById(workerRequestDto.getExpertiseIn())){
            throw new RuntimeException("Something went wrong while creating worker");
        }else {
            Services service = servicesRepository.findById(workerRequestDto.getExpertiseIn()).orElseThrow();
            worker.setService(service);
        }
        Worker savedWorker = workerRepository.save(worker);
        log.info("savedWorker:{}",savedWorker);
       WorkerResponseDto workerResponse = modelMapper.map(savedWorker,WorkerResponseDto.class);
       log.info("worker response:{}",workerResponse);
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
    public UserResponse getAllUsers(int pageNumber, String searchInput, Boolean isBlocked) {
        Pageable pageable = PageRequest.of(pageNumber,8);
        Page<User> usersPage;
        Long totalNumberOfUsers = null;
        if(searchInput != null && isBlocked !=null){
            totalNumberOfUsers = isBlocked ?
                    userRepository.countByRoleAndIsBlockedBySearch(Roles.USER, searchInput) :
                    userRepository.countByRoleAndNotBlockedBySearch(Roles.USER, searchInput);
            usersPage = isBlocked ? userRepository.findByRoleAndIsBlockedBySearch(Roles.USER,searchInput,pageable):userRepository.findByRoleAndNotBlockedBySearch(Roles.USER, searchInput,pageable);
        }else if(searchInput == null && isBlocked != null){
            usersPage = isBlocked==true ? userRepository.findByRoleAndNotBlocked(Roles.USER,pageable) : userRepository.findByRoleAndIsBlocked(Roles.USER,pageable);
            totalNumberOfUsers = isBlocked ?
                    userRepository.countByRoleAndNotBlocked(Roles.USER) :
                    userRepository.countByRoleAndIsBlocked(Roles.USER);
        }else if (searchInput != null && isBlocked == null){
            totalNumberOfUsers = userRepository.countByRoleAndSearchInput(Roles.USER, searchInput);
            usersPage = userRepository.findByRoleAndSearchInput(Roles.USER,searchInput,pageable);
        }else {
            usersPage = userRepository.findByRole(Roles.USER, pageable);
        }
        List<UserResponseDto> userResponseDtos = usersPage.stream().map(user ->  new UserResponseDto(user)).collect(Collectors.toList());
        Integer totalNumberOfPages = (int) Math.ceil((double) totalNumberOfUsers / 8);
        return new UserResponse(userResponseDtos,totalNumberOfPages);

    }



    @Override
    public Boolean blockUser(String email) {
        User user = userRepository.findByEmail(email);
        if(user.isBlocked()){
            user.setBlocked(false);
        }else {
            user.setBlocked(true);
        }
        boolean response = authServiceClient.blockUser(email);
        userRepository.save(user);
        return user.isBlocked() ?  false : true;

    }
    @Override
    public WorkerResponse getAllWorkers(Integer pageNumber, String searchInput, Boolean isBlocked, Long serviceId, Integer pageSize) {
        if(pageSize == null){
            pageSize=8;
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Worker> workers= workerRepository.findAllWorkers(serviceId,searchInput,isBlocked,pageable);
        Integer totalNumberOfWorkers = workerRepository.countAllWorkers(serviceId,searchInput,isBlocked);
        List<WorkerResponseDto> workerResponses = workers.stream().map(worker  -> new WorkerResponseDto(worker)).toList();
        System.out.println("totalworkers---:"+totalNumberOfWorkers);
        System.out.println("pageSize  :  " +pageSize);
        Integer totalNumberOfPages = (int) Math.ceil((double) totalNumberOfWorkers / pageSize);
        WorkerResponse workerResponse = new WorkerResponse(workerResponses,totalNumberOfPages);
        return workerResponse;
    }
    @Override
    public List<WorkerResponseDto> getTopRatedWorkers() {
//        try {
            Pageable pageable = PageRequest.of(0,8);
            Page<Worker> workers = workerRepository.findAll(pageable);
            List<WorkerResponseDto> workerResponses = workers.stream().map(worker  -> new WorkerResponseDto(worker)).toList();
            return workerResponses;
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }
    }

    @Override
    public Boolean blockWorker(String email) {
        Worker worker = workerRepository.findByEmail(email);
        if(worker.isBlocked()){
            worker.setBlocked(false);
        }else {
            worker.setBlocked(true);
        }
        boolean response = authServiceClient.blockUser(email);
        workerRepository.save(worker);
        return worker.isBlocked()?false:true;
    }


    @Override
    public UserResponseDto addAddress(AddressDto addressDto, String email) {
        Address address = new Address(addressDto);
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
    public WorkerResponseDto getWorkerDetailsByIdOrEmail(Long id,String email) {
        System.out.println(email);
        if(id != null) return modelMapper.map(workerRepository.findById(id),WorkerResponseDto.class);
        if(email != null) return modelMapper.map(workerRepository.findByEmail(email),WorkerResponseDto.class);
        else return null;
    }




}
