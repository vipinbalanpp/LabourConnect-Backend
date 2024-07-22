package com.example.booking.repository;

import com.example.booking.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    Page<Booking> findByWorkerId(Long workerId, Pageable pageable);
    List<Booking> findByWorkerId(Long workerId);

    Page<Booking> findByUserId(Long userId,Pageable pageable);

    int countByWorkerId(Long workerId);

    int countByUserId(Long userId);
}