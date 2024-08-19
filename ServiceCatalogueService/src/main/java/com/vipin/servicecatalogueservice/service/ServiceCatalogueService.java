package com.vipin.servicecatalogueservice.service;
import com.vipin.servicecatalogueservice.model.dto.ServiceCatalogueDto;
import com.vipin.servicecatalogueservice.model.dto.ServiceCataloguesResponseDto;

import java.util.List;

public interface ServiceCatalogueService {
    ServiceCatalogueDto createService(ServiceCatalogueDto serviceDto);

    ServiceCataloguesResponseDto getAllServices(Integer pageNumber, Integer pageSize, String searchInput);
    List<ServiceCatalogueDto> getAllServices();


    ServiceCatalogueDto editService(String serviceName, ServiceCatalogueDto serviceDto);

    ServiceCatalogueDto getServiceDetailByServiceId(Long serviceId,String serviceName);

}