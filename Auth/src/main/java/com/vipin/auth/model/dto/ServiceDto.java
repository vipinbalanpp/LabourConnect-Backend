package com.vipin.auth.model.dto;

import com.vipin.auth.model.response.WorkerResponseDto;
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
    private String serviceName;
    private String logo;
    private  String description;
    private List<WorkerResponseDto> workers;
}
