package com.vipin.servicecatalogueservice.controller;

import com.vipin.servicecatalogueservice.model.dto.ServiceCatalogueDto;
import com.vipin.servicecatalogueservice.model.dto.ServiceCataloguesResponseDto;
import com.vipin.servicecatalogueservice.service.ServiceCatalogueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ServiceCatalogueController {
    private final ServiceCatalogueService serviceCatalogueService;
    @GetMapping("/get-all-services")
    public ResponseEntity<ServiceCataloguesResponseDto> getAllServicesByPageNumber(
            @RequestParam Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchInput) {
        ServiceCataloguesResponseDto response = serviceCatalogueService.getAllServices(pageNumber, pageSize, searchInput);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/serviceDetail")
    public ResponseEntity<ServiceCatalogueDto> getServiceDetail(@RequestParam(required = false) Long  serviceId,
                                                                @RequestParam(required = false) String serviceName){
        return new ResponseEntity<>( serviceCatalogueService.getServiceDetailByServiceId(serviceId,serviceName),HttpStatus.OK);
    }
    @GetMapping("/services")
    public ResponseEntity<List<ServiceCatalogueDto>> getAllServices(){
        return new ResponseEntity<>(serviceCatalogueService.getAllServices(),HttpStatus.OK);
    }
    @PostMapping("/create-service")
    public ResponseEntity<ServiceCatalogueDto> createService (@RequestBody ServiceCatalogueDto serviceCatalogueDto){
        System.out.println(serviceCatalogueDto.getServiceName());
        ServiceCatalogueDto service =  serviceCatalogueService.createService(serviceCatalogueDto);
        return new ResponseEntity(service,HttpStatus.CREATED);
    }
    @PutMapping("/edit-service")
    public ResponseEntity<ServiceCatalogueDto> editService (@RequestBody ServiceCatalogueDto serviceDto,
                                                   @RequestParam String serviceName){
        ServiceCatalogueDto service =  serviceCatalogueService.editService(serviceName,serviceDto);
        return new ResponseEntity(service,HttpStatus.CREATED);
    }

}
