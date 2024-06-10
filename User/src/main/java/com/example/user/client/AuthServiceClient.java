package com.example.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authServiceClient",url = "http://localhost:8081")
public interface AuthServiceClient {
    @PutMapping("/auth/api/v1/blockUser")
    boolean blockUser(@RequestParam String email);
    @PutMapping("/auth/api/v1/unBlockUser")
    boolean unBlockUser(@RequestParam String email);
}
