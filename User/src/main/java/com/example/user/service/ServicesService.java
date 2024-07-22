package com.example.user.service;

import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.response.ServiceResponse;

import java.util.List;

public interface ServicesService {
    ServiceDto createService(ServiceDto serviceDto);

    ServiceResponse getAllServices(Integer pageNumber,Integer pageSize,String searchInput);
    List<ServiceDto> getAllServices();


    ServiceDto editService(String serviceName, ServiceDto serviceDto);

    ServiceDto getSErviceDetailByName(String serviceName);
}
