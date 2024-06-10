package com.example.user.repository;
import com.example.user.model.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Services,Long> {

    boolean existsByServiceName(String serviceName);
}
