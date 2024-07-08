package com.vipin.auth.client;

import com.vipin.auth.model.dto.UserCreationRequest;
import com.vipin.auth.model.dto.UserResponseDto;
import com.vipin.auth.model.dto.WorkerCreationRequest;
import com.vipin.auth.model.response.WorkerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userServiceClient",url = "http://localhost:8083")
public interface UserServiceClient {
    @PostMapping("/user/api/v1/createUser")
    UserResponseDto createUser(@RequestBody UserCreationRequest userCreationRequest);
    @PostMapping("/user/api/v1/createWorker")
    WorkerResponseDto createWorker(@RequestBody WorkerCreationRequest workerCreationRequest);
}
