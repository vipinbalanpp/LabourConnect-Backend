package com.example.user.service;
import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.EditWorkerRequestDto;
import com.example.user.model.dto.request.UserRequestDto;
import com.example.user.model.dto.request.WorkerRequestDto;
import com.example.user.model.dto.response.UserResponseDto;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequest);

    WorkerResponseDto createWorker(WorkerRequestDto workerRequest);

    UserResponseDto getUserDetails(String email);

    WorkerResponseDto getWorkerDetails(String email);

    User getUserByEmail(String email);

    List<UserResponseDto> getAllUsers(int pageNumber,String searchInput,Boolean isBlocked);

    void blockUser(String email);

    void unBlockUser(String email);

    List<WorkerResponseDto> getAllWorkers(Integer pageNumber,String searchInput,Boolean isBlocked,Long serviceId,Integer pageSize);

    void blockWorker(String email);

    void unBlockWorker(String email);

    UserResponseDto addAddress(AddressDto addressDto, String email);

    void editWorkerDetails(EditWorkerRequestDto editWorkerDto, String email);

    void changeProfileImageUrl(String role, String email, String profileImageUrl);

    void editAddress(AddressDto addressDto, HttpServletRequest request);

    void editFullName(String fullName, HttpServletRequest request);

    void addUserAddress(AddressDto addressDto, HttpServletRequest request);

    UserResponseDto getUserDetailsById(Long id);

    WorkerResponseDto getWorkerDetailsById(Long id);

    Integer getTotalPageNumbersOfUsers(String searchInput, Boolean isBlocked);

    Integer getTotalPageNumbersOfWorkers(Long serviceId, String searchInput, Boolean isBlocked,Integer pageSize);
}