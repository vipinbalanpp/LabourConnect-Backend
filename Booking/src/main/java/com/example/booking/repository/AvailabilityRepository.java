package com.example.booking.repository;

import com.example.booking.model.entity.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    Availability findByWorkerId(Long workerId);

    boolean existsByWorkerId(long workerId);
}
