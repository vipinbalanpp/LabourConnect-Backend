package com.example.user.service.impl;
import com.example.user.client.AuthServiceClient;
import com.example.user.client.ServiceCatalogueServiceClient;
import com.example.user.excepation.UserNotFoundException;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.*;
import com.example.user.repository.AddressRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WorkerRepository;
import com.example.user.service.UserService;
import com.example.user.service.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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
    private final ServiceCatalogueServiceClient serviceCatalogueServiceClient;
    @Override
    public UserResponseDto createUser(UserRequestDto userRequest) {
       User user = new User(userRequest);
       UserResponseDto userResponse = modelMapper.map(userRepository.save(user),UserResponseDto.class);
       return userResponse;
    }

    @Override
    @Transactional
    public WorkerResponseDto createWorker(WorkerRequestDto workerRequestDto) {
        Address address =  new Address(workerRequestDto.getHouseName(),workerRequestDto.getStreet(),workerRequestDto.getCity(),workerRequestDto.getState(),workerRequestDto.getPinCode());
        addressRepository.save(address);
        Worker worker = new Worker(workerRequestDto);
        worker.setAddress(address);
        worker.setServiceId(workerRequestDto.getServiceId());
        Worker savedWorker = workerRepository.save(worker);
        ServiceDto serviceDto = serviceCatalogueServiceClient.getServiceDetail(savedWorker.getServiceId());
       WorkerResponseDto workerResponse = modelMapper.map(savedWorker,WorkerResponseDto.class);
       workerResponse.setService(serviceDto);
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
        WorkerResponseDto workerResponseDto =  modelMapper.map(worker,WorkerResponseDto.class);
        ServiceDto serviceDto = serviceCatalogueServiceClient.getServiceDetail(worker.getServiceId());
        if (serviceDto != null) {
            workerResponseDto.setService(serviceDto);
        } else {
            throw new RuntimeException("Something went wrong");
        }
       return workerResponseDto;
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
    public WorkerResponse getAllWorkers(Integer pageNumber, String searchInput, Boolean isBlocked, Long serviceId, Integer pageSize, String priceSort,String experienceSort) {
        if (pageSize == null) {
            pageSize = 8;
        }
        Sort sort = Sort.unsorted();
        if (priceSort != null) {
            sort = Sort.by("serviceCharge");
            if (priceSort.equalsIgnoreCase("lowToHigh")) {
                sort = sort.ascending();
            } else if (priceSort.equalsIgnoreCase("highToLow")) {
                sort = sort.descending();
            }
        }
        if (experienceSort != null) {
            Sort experienceSorting = Sort.by("experience");
            if (experienceSort.equalsIgnoreCase("lowToHigh")) {
                sort = sort.ascending();
            } else if (experienceSort.equalsIgnoreCase("highToLow")) {
                sort = sort.descending();
            }
            sort = sort.and(experienceSorting);
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Worker> workers = workerRepository.findAllWorkers(serviceId, searchInput, isBlocked, pageable);
        List<WorkerResponseDto> workerResponses = workers.stream().map(worker -> {
            WorkerResponseDto workerResponseDto = new WorkerResponseDto(worker);
            ServiceDto serviceDto = serviceCatalogueServiceClient.getServiceDetail(worker.getServiceId());
            workerResponseDto.setService(serviceDto);
            return workerResponseDto;
        }).toList();
        WorkerResponse workerResponse = new WorkerResponse(workerResponses, workers.getTotalPages());
        return workerResponse;
    }

    @Override
    public List<WorkerResponseDto> getTopRatedWorkers() {
            Pageable pageable = PageRequest.of(0,6);
            Page<Worker> workers = workerRepository.findAll(pageable);
            List<WorkerResponseDto> workerResponses = workers.stream().map(worker  -> {
               WorkerResponseDto workerResponseDto = new WorkerResponseDto(worker);
               ServiceDto serviceDto  = serviceCatalogueServiceClient.getServiceDetail(worker.getServiceId());
               workerResponseDto.setService(serviceDto);
              return   workerResponseDto;
            }).toList();
            return workerResponses;
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
        address.setPinCode(addressDto.getPinCode());
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
        address.setPinCode(addressDto.getPinCode());
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
        Worker worker ;
        if(id != null) {
            worker =  workerRepository.findById(id).orElseThrow();
        }
       else if(email != null) {
            worker =  workerRepository.findByEmail(email);
        }else  throw  new RuntimeException("Something went wrong");
        WorkerResponseDto workerResponseDto = new WorkerResponseDto(worker);
        ServiceDto serviceDto = serviceCatalogueServiceClient.getServiceDetail(worker.getServiceId());
        workerResponseDto.setService(serviceDto);
        return workerResponseDto;
    }




}
