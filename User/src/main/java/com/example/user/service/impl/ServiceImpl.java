package com.example.user.service.impl;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.Services;
import com.example.user.model.entity.Worker;
import com.example.user.repository.ServicesRepository;
import com.example.user.repository.WorkerRepository;
import com.example.user.service.ServicesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements ServicesService {
    private final ServicesRepository servicesRepository;
    private final WorkerRepository workerRepository;
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
        System.out.println(service.getServiceName() +"----------------------->");
        servicesRepository.save(service);
        return modelMapper.map(service,ServiceDto.class);
    }

    @Override
    public List<ServiceDto> getAllServices(int pageNumber) {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Services> services = servicesRepository.findAll(pageable);
        List<ServiceDto> serviceDtos = new ArrayList<>();
        for(Services service : services){
            ServiceDto serviceDto = modelMapper.map(service,ServiceDto.class);
            List<Worker> workers = workerRepository.findByService(servicesRepository.findByServiceName(service.getServiceName()));
                        serviceDto.setWorkers(workers);
            System.out.println(serviceDto.getServiceName());
           serviceDtos.add(serviceDto);
        }
        return serviceDtos;
    }

    @Override
    public List<ServiceDto> getAllServices() {
        List<Services> services = servicesRepository.findAll();
        List<ServiceDto> serviceDtos = new ArrayList<>();
        for(Services service : services){
            ServiceDto serviceDto = modelMapper.map(service,ServiceDto.class);
            List<Worker> workers = workerRepository.findByService(servicesRepository.findByServiceName(service.getServiceName()));
            serviceDto.setWorkers(workers);
            System.out.println(serviceDto.getServiceName());
            serviceDtos.add(serviceDto);
        }
        return serviceDtos;
    }

    @Override
    public ServiceDto editService(String serviceName, ServiceDto serviceDto) {
       if(!servicesRepository.existsByServiceName(serviceName)){
           throw new EntityNotFoundException("Service "+serviceName+ " does not exits");
       }
       if(servicesRepository.existsByServiceName(serviceDto.getServiceName())){
           throw new RuntimeException("Service exists with name" + serviceDto.getServiceName());
       }
           Services service = servicesRepository.findByServiceName(serviceName);
           service.setLogo(serviceDto.getLogo());
           service.setServiceName(serviceDto.getServiceName());
           service.setDescription(serviceDto.getDescription());
           servicesRepository.save(service);
           return serviceDto;

    }
}
