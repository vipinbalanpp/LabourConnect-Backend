package com.vipin.servicecatalogueservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCataloguesResponseDto {
    private List<ServiceCatalogueDto> services;
    private Integer totalNumberOfPages;
}
