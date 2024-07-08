package com.example.booking.service;

import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.entity.Booking;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    void createBooking(BookingDto bookingDto);

    List<BookingResponseDto> getAllBookings();

    BookingResponseDto reScheduleBooking(String bookingId, boolean isWorker);
}
