package com.example.user.service;

import com.example.user.model.dto.ServiceDto;

import java.util.List;

public interface ServicesService {
    ServiceDto createService(ServiceDto serviceDto);

    List<ServiceDto> getAllServices();
}