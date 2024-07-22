package com.example.user.service.impl;
import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.response.ServiceResponse;
import com.example.user.model.entity.Services;
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
import java.util.List;
import java.util.stream.Collectors;

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
    public ServiceResponse getAllServices(Integer pageNumber,Integer pageSize,String searchInput) {
        if(pageSize == null){
            pageSize = 8;
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Services> services;
     if (searchInput != null && !searchInput.isEmpty()) {
            services = servicesRepository.findByServiceNameStartingWith(searchInput, pageable);
        } else {
            services = servicesRepository.findAll(pageable);
        }
        List<ServiceDto> serviceDtos =  services.stream().map(service->new ServiceDto(service)).collect(Collectors.toList());
        Integer totalNumberOfPages = Math.toIntExact(servicesRepository.count()/pageSize);
        ServiceResponse serviceResponse = new ServiceResponse(serviceDtos,totalNumberOfPages);
        return serviceResponse;
    }

    @Override
    public List<ServiceDto> getAllServices() {
        return servicesRepository.findAll().stream().map(service -> new ServiceDto(service)).toList();
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

    @Override
    public ServiceDto getSErviceDetailByName(String serviceName) {
        if(!servicesRepository.existsByServiceName(serviceName))throw new EntityNotFoundException("Service Not found");
        return new ServiceDto(servicesRepository.findByServiceName(serviceName));
    }
}
