package com.example.user.service;

import com.example.user.model.dto.AddressDto;
import com.example.user.model.dto.request.UserRequest;
import com.example.user.model.dto.response.UserResponse;
import com.example.user.model.dto.request.WorkerRequest;
import com.example.user.model.dto.response.WorkerResponse;
import com.example.user.model.entity.User;
import com.example.user.model.entity.Worker;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    WorkerResponse createWorker(WorkerRequest workerRequest);

    UserResponse getUserDetails(String email);

    WorkerResponse getWorkerDetails(String email);

    User getUserByEmail(String email);

    List<UserResponse> getAllUsers();

    void blockUser(String email);

    void unBlockUser(String email);

    List<WorkerResponse> getAllWorkers();

    void blockWorker(String email);

    void unBlockWorker(String email);

    UserResponse addAddress(AddressDto addressDto, String email);
}