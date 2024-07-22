package com.example.user.model.dto;

import com.example.user.model.dto.response.WorkerResponseDto;
import com.example.user.model.entity.Services;
import com.example.user.model.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long serviceId;
    private String serviceName;
    private String logo;
    private String description;

    public ServiceDto(Services services){
        this.setServiceId(services.getServiceId());
        this.setServiceName(services.getServiceName());
        this.setLogo(services.getLogo());
        this.setDescription(services.getDescription());

    }
}
