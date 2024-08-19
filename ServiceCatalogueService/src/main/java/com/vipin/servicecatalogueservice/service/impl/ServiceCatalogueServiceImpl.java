package com.vipin.servicecatalogueservice.service.impl;

import com.vipin.servicecatalogueservice.model.dto.ServiceCatalogueDto;
import com.vipin.servicecatalogueservice.model.dto.ServiceCataloguesResponseDto;
import com.vipin.servicecatalogueservice.model.entity.ServiceCatalogue;
import com.vipin.servicecatalogueservice.repository.ServiceCatalogueRepository;
import com.vipin.servicecatalogueservice.service.ServiceCatalogueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCatalogueServiceImpl implements ServiceCatalogueService {
    private final ServiceCatalogueRepository serviceCatalogueRepository;
    @Override
    public ServiceCatalogueDto createService(ServiceCatalogueDto serviceDto) {
        if (serviceCatalogueRepository.existsByServiceName(serviceDto.getServiceName())) {
            throw new RuntimeException("Service with name " + serviceDto.getServiceName() + " already exists.");
        }
        ServiceCatalogue service = new ServiceCatalogue(serviceDto);
        ServiceCatalogue savedService = serviceCatalogueRepository.save(service);
        return new ServiceCatalogueDto(savedService);
    }


    @Override
    public ServiceCataloguesResponseDto getAllServices(Integer pageNumber, Integer pageSize, String searchInput) {
        if (pageSize == null) {
            pageSize = 8;
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ServiceCatalogue> services;
        if (searchInput != null && !searchInput.isEmpty()) {
            services = serviceCatalogueRepository.findByServiceNameStartingWith(searchInput, pageable);
        } else {
            services = serviceCatalogueRepository.findAll(pageable);
        }
        List<ServiceCatalogueDto> serviceCatalogueDtos = services.stream()
                .map(ServiceCatalogueDto::new)
                .collect(Collectors.toList());

        return new ServiceCataloguesResponseDto(serviceCatalogueDtos, services.getTotalPages());
    }


    @Override
    public List<ServiceCatalogueDto> getAllServices() {
        return serviceCatalogueRepository.findAll().stream().map(service -> new ServiceCatalogueDto(service)).toList();

    }

    @Override
    public ServiceCatalogueDto editService(String serviceName, ServiceCatalogueDto serviceDto) {
        ServiceCatalogue service = serviceCatalogueRepository.findByServiceName(serviceName);
        if (service == null) {
            throw new EntityNotFoundException("Service " + serviceName + " does not exist");
        }
        if (!serviceName.equals(serviceDto.getServiceName()) &&
                serviceCatalogueRepository.existsByServiceName(serviceDto.getServiceName())) {
            throw new RuntimeException("Service exists with name " + serviceDto.getServiceName());
        }
        service.setImage(serviceDto.getImage());
        service.setServiceName(serviceDto.getServiceName());
        service.setDescription(serviceDto.getDescription());
        ServiceCatalogue updatedService = serviceCatalogueRepository.save(service);
        return new ServiceCatalogueDto(updatedService);
    }


    @Override
    public ServiceCatalogueDto getServiceDetailByServiceId(Long serviceId , String serviceName) {
        if(serviceId == null && serviceName == null){
            throw new RuntimeException("Something went wrong");
        }if(serviceId != null){
        if(!serviceCatalogueRepository.existsByServiceId(serviceId))throw new EntityNotFoundException("Service Not found");
        return new ServiceCatalogueDto(serviceCatalogueRepository.findByServiceId(serviceId));
        }else {
            if(!serviceCatalogueRepository.existsByServiceName(serviceName))throw new EntityNotFoundException("Service Not found");
            return new ServiceCatalogueDto(serviceCatalogueRepository.findByServiceName(serviceName));
        }
    }
}
