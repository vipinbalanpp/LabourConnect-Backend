package com.example.booking.repository;

import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface BookingRepositoryCustom {
    Page<Booking> findBookingsByCriteria(Long workerId,Long userId,Status status,Date workDate,Long serviceId,Pageable pageable);
}
