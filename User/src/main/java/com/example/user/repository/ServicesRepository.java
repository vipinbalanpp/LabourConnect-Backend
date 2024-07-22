package com.example.user.repository;
import com.example.user.model.entity.Services;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    boolean existsByServiceName(String serviceName);

    @Query("SELECT s FROM Services s WHERE LOWER(s.serviceName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    Page<Services> findByServiceNameStartingWith(@Param("searchInput") String searchInput, Pageable pageable);

    Services findByServiceName(String serviceName);
}