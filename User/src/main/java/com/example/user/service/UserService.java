package com.example.user.service;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequest);

    WorkerResponseDto createWorker(WorkerRequestDto workerRequest);

    UserResponseDto getUserDetails(String email);

    WorkerResponseDto getWorkerDetails(String email);

    User getUserByEmail(String email);

    UserResponse getAllUsers(int pageNumber, String searchInput, Boolean isBlocked);

    Boolean blockUser(String email);


    WorkerResponse getAllWorkers(Integer pageNumber, String searchInput, Boolean isBlocked, Long serviceId, Integer pageSize, String priceSort,String experienceSort);

    Boolean blockWorker(String email);


    UserResponseDto addAddress(AddressDto addressDto, String email);

    void editWorkerDetails(EditWorkerRequestDto editWorkerDto, String email);

    void changeProfileImageUrl(String role, String email, String profileImageUrl);

    void editAddress(AddressDto addressDto, HttpServletRequest request);

    void editFullName(String fullName, HttpServletRequest request);

    void addUserAddress(AddressDto addressDto, HttpServletRequest request);

    UserResponseDto getUserDetailsById(Long id);

    WorkerResponseDto getWorkerDetailsByIdOrEmail(Long id,String email);

    List<WorkerResponseDto> getTopRatedWorkers();
}