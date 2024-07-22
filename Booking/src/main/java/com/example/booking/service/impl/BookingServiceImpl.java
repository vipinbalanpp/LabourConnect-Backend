package com.example.booking.service.impl;

import com.example.booking.model.dto.*;
import com.example.booking.model.entity.Status;
import com.example.booking.repository.BookingRepository;
import com.example.booking.client.UserServiceClient;
import com.example.booking.model.entity.Booking;
import com.example.booking.service.BookingService;
import com.example.booking.service.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserServiceClient userServiceClient;
    @Override
    public void createBooking(BookingDto bookingDto) {
        System.out.println(bookingDto.getWorkDate());
        System.out.println(bookingDto.getBookingDate());
        Booking booking = Booking.builder()
                .userId(bookingDto.getUserId())
                .workerId(bookingDto.getWorkerId())
                .workDescription(bookingDto.getWorkDescription())
                .bookingDate(bookingDto.getBookingDate())
                .workDate(bookingDto.getWorkDate())
                .status(Status.REQUESTED)
                .serviceCharge(bookingDto.getServiceCharge())
                .cancellationReason(bookingDto.getCancellationReason())
                .cancelledBy(bookingDto.getCancelledBy())
                .workLocationAddress(bookingDto.getWorkAddress())
                .build();
        bookingRepository.save(booking);
    }


    public List<BookingResponseDto> mapBookingToResponse(Page<Booking> bookings){
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        for(Booking booking:bookings){
            BookingResponseDto bookingDto = new BookingResponseDto(booking);
            bookingResponseDtos.add(bookingDto);
            UserDto userDto = userServiceClient.getUserDetailsById(Long.valueOf(booking.getUserId()));
            bookingDto.setUser(userDto);
            WorkerDto workerDto = userServiceClient.getWorkerDetailsByIdOrEmail(booking.getWorkerId());
            bookingDto.setWorker(workerDto);
        }
        Collections.reverse(bookingResponseDtos);
        return bookingResponseDtos;
    }

    @Override
    public BookingResponseDto reScheduleBooking(String bookingId, Boolean isWorker) {
//      Booking booking = bookingRepository.findById(bookingId).orElseThrow();
//      booking.setStatus(Status.REQUESTED_FOR_RESCHEDULE);
//      BookingResponseDto bookingResponseDto = new BookingResponseDto(booking);
//      bookingResponseDto.setWorker(w);
        return null;

    }

    @Override
    public BookingsResponse getAllBookingsOfUser(Long userId,Integer pageNumber) {
        Integer pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Booking> bookings = bookingRepository.findByUserId(userId,pageable);
        List<BookingResponseDto>  bookingResponseDtos;
        if(bookings != null){
            bookingResponseDtos =mapBookingToResponse(bookings);
        }
        else bookingResponseDtos= null;
        int totalPages = (int) Math.ceil((double) bookingRepository.countByUserId(userId) / pageSize);
        return new BookingsResponse(bookingResponseDtos, totalPages);
    }

    @Override
    public BookingsResponse getAllBookingsOfWorker(Long workerId,Integer pageNumber) {
        Integer pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Booking> bookings = bookingRepository.findByWorkerId(workerId,pageable);
        List<BookingResponseDto>  bookingResponseDtos;
        if(bookings != null){
            bookingResponseDtos =    mapBookingToResponse(bookings);
        }
        else bookingResponseDtos= null;
        int totalPages = (int) Math.ceil((double) bookingRepository.countByWorkerId(workerId) / pageSize);
        return new BookingsResponse(bookingResponseDtos,totalPages);
    }
}
