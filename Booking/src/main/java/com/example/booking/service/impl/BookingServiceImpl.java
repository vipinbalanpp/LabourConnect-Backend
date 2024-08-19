package com.example.booking.service.impl;

import com.example.booking.exceptions.EntityNotFoundException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserServiceClient userServiceClient;
    @Override
    public void createBooking(BookingDto bookingDto) {
        if(bookingRepository.existsByWorkerIdAndUserIdAndWorkDate(bookingDto.getWorkerId(),bookingDto.getUserId(),bookingDto.getWorkDate())){
            Booking booking = bookingRepository.findByWorkerIdAndUserIdAndWorkDate(bookingDto.getWorkerId(),bookingDto.getUserId(),bookingDto.getBookingDate());
            if(booking.getStatus().equals(Status.CONFIRMED)){
                throw new RuntimeException("You already having a booking on this date");
            }else if(booking.getStatus().equals(Status.REQUESTED)){
                throw new RuntimeException("You already requested for service");
            }
        }
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


    public List<BookingResponseDto> mapBookingsToResponse(Page<Booking> bookings){
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
    public BookingResponseDto mapBookingToResponse(Booking booking){
            BookingResponseDto bookingDto = new BookingResponseDto(booking);
            UserDto userDto = userServiceClient.getUserDetailsById(Long.valueOf(booking.getUserId()));
            bookingDto.setUser(userDto);
            WorkerDto workerDto = userServiceClient.getWorkerDetailsByIdOrEmail(booking.getWorkerId());
            bookingDto.setWorker(workerDto);
        return bookingDto;
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
            bookingResponseDtos =mapBookingsToResponse(bookings);
        }
        else bookingResponseDtos= null;
        int totalPages = (int) Math.ceil((double) bookingRepository.countByUserId(userId) / pageSize);
        return new BookingsResponse(bookingResponseDtos, totalPages);
    }

    @Override
    public BookingsResponse getAllBookingsOfWorker(Long workerId, Status status, Date workDate, Long serviceId, Integer pageNumber) {
        Integer pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Booking> bookings = bookingRepository.findBookingsByWorkerIdAndCriteria(
               workerId,
               status,
                workDate,
                serviceId,
                pageable
        );
        List<BookingResponseDto>  bookingResponseDtos;
        if(bookings != null){
            bookingResponseDtos =    mapBookingsToResponse(bookings);
        }
        else bookingResponseDtos= null;
        int totalPages = (int) Math.ceil((double) bookingRepository.countByWorkerId(workerId) / pageSize);
        return new BookingsResponse(bookingResponseDtos,totalPages);
    }
    @Scheduled(cron = "0 34 16 * * ?")
    public void autoCompleteWork() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        System.out.println("Schedule working");
        List<Booking> bookings = bookingRepository.findByStatusAndWorkDate(Status.CONFIRMED,tomorrow);
        System.out.println("Bookings found: " + bookings.size());
        for (Booking booking : bookings) {
            booking.setStatus(Status.COMPLETED);
            System.out.println(      booking.getWorkDate().getClass());
            bookingRepository.save(booking);
        }
    }




    @Override
    @Transactional
    public BookingResponseDto cancelBooking(String bookingId,   String cancellationReason,String cancelledBy) {
     try {
         if(!bookingRepository.existsById(bookingId)){
             throw  new EntityNotFoundException("Booking not found");
         }
         Booking booking = bookingRepository.findById(bookingId).orElseThrow();
         booking.setStatus(Status.CANCELLED);
         if(cancelledBy.equals("user"))
             booking.setCancelledBy("user");
         else if(cancelledBy.equals("worker"))
             booking.setCancelledBy("worker");
         else
             throw new RuntimeException("Something went wrong");
         booking.setCancellationReason(cancellationReason);
         bookingRepository.save(booking);
         return mapBookingToResponse(booking);
     }catch (Exception e){
         throw new RuntimeException("Something went wrong while cancelling booking");
     }
    }

    @Override
    public void confirmBooking(String bookingId) {
       try {
           if(!bookingRepository.existsById(bookingId)){
               throw  new EntityNotFoundException("Booking not found");
           }
           Booking booking = bookingRepository.findById(bookingId).orElseThrow();
           booking.setStatus(Status.CONFIRMED);
           bookingRepository.save(booking);
       }catch (Exception e){
           throw new RuntimeException("Something went wrong while confirming booking");

       }

    }


    @Override
    public void rejectBooking(String bookingId,String reasonForRejection) {
        try {
            if(!bookingRepository.existsById(bookingId)){
                throw  new EntityNotFoundException("Booking not found");
            }
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setStatus(Status.REJECTED);
            booking.setReasonForRejection(reasonForRejection);
            bookingRepository.save(booking);
        }catch (Exception e){
            throw new RuntimeException("Something went wrong while confirming booking");

        }
    }
}
