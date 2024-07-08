package com.example.user.controller;

import com.example.user.model.dto.ServiceDto;
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
    public ResponseEntity<List<ServiceDto>> getAllServicesByPageNumber(@RequestParam int pageNumber){
        try {
            List<ServiceDto> services = servicesService.getAllServices(pageNumber);
            return new ResponseEntity<>(services, HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @GetMapping("/get-services")
    public ResponseEntity<List<ServiceDto>> getAllServices(){
        try {
            List<ServiceDto> services = servicesService.getAllServices();
            return new ResponseEntity<>(services, HttpStatus.OK);
        }catch (RuntimeException e){
            throw  new RuntimeException("Something went wrong");
        }
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
