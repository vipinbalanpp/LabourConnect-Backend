package com.vipin.servicecatalogueservice.model.entity;
import com.vipin.servicecatalogueservice.model.dto.ServiceCatalogueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCatalogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    private String serviceName;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String description;

    public ServiceCatalogue (ServiceCatalogueDto serviceCatalogueDto){
        this.serviceName = serviceCatalogueDto.getServiceName();
        this.image = serviceCatalogueDto.getImage();
        this.description = serviceCatalogueDto.getDescription();
    }

}
