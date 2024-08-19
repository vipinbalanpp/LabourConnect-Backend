package com.example.user.client;

import com.example.user.model.dto.ServiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "serviceCatalogueServiceClient",url = "http://localhost:8085")
public interface ServiceCatalogueServiceClient {
    @GetMapping("/service/api/v1/serviceDetail")
    ServiceDto getServiceDetail(@RequestParam Long serviceId);

}
