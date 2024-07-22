package com.example.user.model.dto.response;

import com.example.user.model.dto.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private List<ServiceDto> services;
    private Integer totalNumberOfPages;
}
