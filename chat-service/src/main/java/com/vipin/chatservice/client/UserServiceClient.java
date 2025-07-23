package com.vipin.chatservice.client;


import com.vipin.chatservice.model.dto.UsersListForChatRequestDto;
import com.vipin.chatservice.model.entity.PersonInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "userServiceClient",url = "http://localhost:8083")
public interface UserServiceClient {
    @PostMapping("/user/api/v1/getUsersDetailsForChat")
     List<PersonInfo>getUsersList(@RequestBody UsersListForChatRequestDto usersListForChatRequestDto);

}
