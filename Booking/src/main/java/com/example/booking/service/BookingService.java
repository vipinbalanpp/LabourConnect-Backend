package com.example.booking.service;

import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.BookingsResponse;
import com.example.booking.model.entity.Booking;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    void createBooking(BookingDto bookingDto);



    BookingResponseDto reScheduleBooking(String bookingId, Boolean isWorker);

    BookingsResponse getAllBookingsOfUser(Long userId,Integer pageNumber);

    BookingsResponse  getAllBookingsOfWorker(Long workerId,Integer pageNumber);
}
