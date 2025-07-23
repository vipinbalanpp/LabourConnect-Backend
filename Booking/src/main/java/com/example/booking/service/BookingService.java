package com.example.booking.service;

import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.BookingsResponse;
import java.time.LocalDate;
import java.util.Date;

public interface BookingService {
    void createBooking(BookingDto bookingDto);
    BookingResponseDto reScheduleBooking(String bookingId, LocalDate rescheduleDate,String status, Boolean isWorker);

    BookingResponseDto cancelBooking(String bookingId,  String cancellationReason,String cancelledBy);

    void confirmBooking(String bookingId);

    void rejectBooking(String bookingId,String reasonForRejection);

    BookingsResponse getAllBookings(Long userId, Long workerId, Integer pageNumber, String status, Date workDate, Long serviceId);

    String updateBooking(String bookingId, String status);
}
