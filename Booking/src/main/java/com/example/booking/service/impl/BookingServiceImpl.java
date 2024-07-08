package com.example.booking.service.impl;

import com.example.booking.model.entity.Status;
import com.example.booking.repository.BookingRepository;
import com.example.booking.client.UserServiceClient;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.UserDto;
import com.example.booking.model.dto.WorkerDto;
import com.example.booking.model.entity.Booking;
import com.example.booking.service.BookingService;
import com.example.booking.service.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserServiceClient userServiceClient;
//    private final ModalMapper modelMapper;
    @Override
    public void createBooking(BookingDto bookingDto) {
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

    @Override
    public List<BookingResponseDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        for(Booking booking:bookings){
            BookingResponseDto bookingDto = new BookingResponseDto(booking);
            bookingResponseDtos.add(bookingDto);
            UserDto userDto = userServiceClient.getUserDetailsById(Long.valueOf(booking.getUserId()));
            bookingDto.setUser(userDto);
            WorkerDto workerDto = userServiceClient.getWorkerDetailsById(booking.getWorkerId());
            bookingDto.setWorker(workerDto);
        }return bookingResponseDtos;
    }

    @Override
    public BookingResponseDto reScheduleBooking(String bookingId, boolean isWorker) {
//      Booking booking = bookingRepository.findById(bookingId).orElseThrow();
//      booking.setStatus(Status.REQUESTED_FOR_RESCHEDULE);
//      BookingResponseDto bookingResponseDto = new BookingResponseDto(booking);
//      bookingResponseDto.setWorker(w);
        return null;

    }


}
