package com.example.booking.repository;
import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> ,BookingRepositoryCustom{
    List<Booking> findByWorkerIdAndStatus(Long workerId,Status status);
    Page<Booking> findByUserId(Long userId,Pageable pageable);

    int countByWorkerId(Long workerId);

    int countByUserId(Long userId);


    List<Booking> findByStatusAndWorkDate(Status status, LocalDate workDate);

    Boolean existsByWorkerIdAndUserIdAndWorkDate(Long workerId, Long userId, LocalDate workDate);

    Booking findByWorkerIdAndUserIdAndWorkDate(Long workerId, Long userId, LocalDate bookingDate);
}