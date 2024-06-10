package com.vipin.auth.client;

import com.vipin.auth.model.dto.UserCreationRequest;
import com.vipin.auth.model.dto.UserResponseDto;
import com.vipin.auth.model.dto.WorkerCreationRequest;
import com.vipin.auth.model.dto.WorkerResponse;
import com.vipin.auth.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userServiceClient",url = "http://localhost:8083")
public interface UserServiceClient {
    @PostMapping("/user/api/v1/createUser")
    UserResponseDto createUser(@RequestBody UserCreationRequest userCreationRequest);
    @PostMapping("/user/api/v1/createWorker")
    WorkerResponse createWorker(@RequestBody WorkerCreationRequest workerCreationRequest);
}
