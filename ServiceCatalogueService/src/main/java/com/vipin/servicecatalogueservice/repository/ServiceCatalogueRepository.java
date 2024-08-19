package com.vipin.servicecatalogueservice.repository;


import com.vipin.servicecatalogueservice.model.entity.ServiceCatalogue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCatalogueRepository extends JpaRepository<ServiceCatalogue, Long> {

    boolean existsByServiceName(String serviceName);

    @Query("SELECT s FROM ServiceCatalogue s WHERE LOWER(s.serviceName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    Page<ServiceCatalogue> findByServiceNameStartingWith(@Param("searchInput") String searchInput, Pageable pageable);

    ServiceCatalogue findByServiceName(String serviceName);

    boolean existsByServiceId(Long serviceId);

    ServiceCatalogue findByServiceId(Long serviceId);
}