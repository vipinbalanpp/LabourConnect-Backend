package com.example.user.controller;

import com.example.user.model.dto.ServiceDto;
import com.example.user.model.dto.response.ServiceResponse;
import com.example.user.service.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
    private  final ServicesService servicesService;

    @GetMapping("/get-all-services")
    public ResponseEntity<ServiceResponse> getAllServicesByPageNumber(@RequestParam Integer pageNumber,
                                                                      @RequestParam(required = false)Integer pageSize,
                                                                      @RequestParam(required = false) String searchInput){
        try {
            ServiceResponse response = servicesService.getAllServices(pageNumber,pageSize,searchInput);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @GetMapping("/serviceDetail")
    public ResponseEntity<ServiceDto> getServiceDetail(@RequestParam String serviceName){
        return new ResponseEntity<>( servicesService.getSErviceDetailByName(serviceName),HttpStatus.OK);
    }
    @GetMapping("/services")
    public ResponseEntity<List<ServiceDto>> getAllServices(){
        return new ResponseEntity<>(servicesService.getAllServices(),HttpStatus.OK);
    }

    @PostMapping("/create-service")
    public ResponseEntity<ServiceDto> createService (@RequestBody ServiceDto serviceDto){
            ServiceDto service =  servicesService.createService(serviceDto);
            return new ResponseEntity<ServiceDto>(service,HttpStatus.CREATED);
    }
    @PutMapping("/edit-service")
    public ResponseEntity<ServiceDto> editService (@RequestBody ServiceDto serviceDto,
                                                   @RequestParam String serviceName){
            ServiceDto service =  servicesService.editService(serviceName,serviceDto);
            return new ResponseEntity<ServiceDto>(service,HttpStatus.CREATED);
    }
}
