package com.vipin.servicecatalogueservice.model.dto;

import com.vipin.servicecatalogueservice.model.entity.ServiceCatalogue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCatalogueDto {
    private Long serviceId;
    private String serviceName;
    private String image;
    private String description;

    public ServiceCatalogueDto(ServiceCatalogue serviceCatalogue){
        this.setServiceId(serviceCatalogue.getServiceId());
        this.setServiceName(serviceCatalogue.getServiceName());
        this.setImage(serviceCatalogue.getImage());
        this.setDescription(serviceCatalogue.getDescription());

    }
}
