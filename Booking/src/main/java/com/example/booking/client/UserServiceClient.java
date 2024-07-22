package com.example.booking.client;
import com.example.booking.model.dto.UserDto;
import com.example.booking.model.dto.WorkerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userServiceClient",url = "http://localhost:8083")
public interface UserServiceClient {
    @GetMapping("/user/api/v1/getUserDetails")
    UserDto getUserDetailsById(@RequestParam Long id);
    @GetMapping("/user/api/v1/workerDetails")
    WorkerDto getWorkerDetailsByIdOrEmail(@RequestParam Long id);
}
