package com.example.user.service.impl;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.entity.Services;
import com.example.user.repository.ServicesRepository;
import com.example.user.service.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements ServicesService {
    private final ServicesRepository servicesRepository;
    private final ModelMapper modelMapper;
    @Override
    public ServiceDto createService(ServiceDto serviceDto) {
        if(servicesRepository.existsByServiceName(serviceDto.getServiceName())){
            throw new RuntimeException("Service Exists");
        }
        Services service = new Services();
        service.setServiceName(serviceDto.getServiceName());
        service.setLogo(serviceDto.getLogo());
        service.setDescription(serviceDto.getDescription());
        servicesRepository.save(service);
        return modelMapper.map(service,ServiceDto.class);
    }

    @Override
    public List<ServiceDto> getAllServices() {
        List<Services> services = servicesRepository.findAll();
        List<ServiceDto> serviceDtos = new ArrayList<>();
        for(Services service : services){
            ServiceDto serviceDto = new ServiceDto();
            serviceDtos.add(modelMapper.map(service,ServiceDto.class));
        }
        return serviceDtos;
    }
}
