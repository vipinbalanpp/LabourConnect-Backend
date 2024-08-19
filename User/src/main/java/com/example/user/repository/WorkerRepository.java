package com.example.user.repository;

import com.example.user.model.entity.Worker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker,Long> {
    Worker findByEmail(String email);

    List<Worker> findByServiceId(Long serviceId);
    @Query("SELECT w FROM Worker w WHERE " +
            "(:serviceId IS NULL OR w.serviceId = :serviceId) AND " +
            "(:searchInput IS NULL OR LOWER(w.fullName) LIKE LOWER(CONCAT( :searchInput, '%'))) AND " +
            "(:isBlocked IS NULL OR w.isBlocked = :isBlocked)")
    Page<Worker> findAllWorkers(
            @Param("serviceId") Long serviceId,
            @Param("searchInput") String searchInput,
            @Param("isBlocked") Boolean isBlocked,
            Pageable pageable
    );

    @Query("SELECT COUNT(w) FROM Worker w WHERE " +
            "(:serviceId IS NULL OR w.serviceId = :serviceId) AND " +
            "(:searchInput IS NULL OR :searchInput = '' OR LOWER(CAST(w.fullName AS string)) LIKE LOWER(CONCAT(:searchInput, '%'))) AND " +
            "(:isBlocked IS NULL OR w.isBlocked = :isBlocked)")
    Integer countAllWorkers(
            @Param("serviceId") Long serviceId,
            @Param("searchInput") String searchInput,
            @Param("isBlocked") Boolean isBlocked
    );

}