package com.example.booking.service;

import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.BookingsResponse;
import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingService {
    void createBooking(BookingDto bookingDto);
    BookingResponseDto reScheduleBooking(String bookingId, LocalDate rescheduleDate, Boolean isWorker);

    BookingResponseDto cancelBooking(String bookingId,  String cancellationReason,String cancelledBy);

    void confirmBooking(String bookingId);

    void rejectBooking(String bookingId,String reasonForRejection);

    BookingsResponse getAllBookings(Long userId, Long workerId, Integer pageNumber, String status, Date workDate, Long serviceId);
}
