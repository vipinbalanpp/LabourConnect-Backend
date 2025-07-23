package com.example.booking.service.impl;

import com.example.booking.exceptions.EntityNotFoundException;
import com.example.booking.model.dto.*;
import com.example.booking.model.entity.Role;
import com.example.booking.model.entity.Status;
import com.example.booking.repository.BookingRepository;
import com.example.booking.client.UserServiceClient;
import com.example.booking.model.entity.Booking;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserServiceClient userServiceClient;
    @Override
    public void createBooking(BookingDto bookingDto) {
        if(bookingRepository.existsByWorkerIdAndUserIdAndWorkDate(bookingDto.getWorkerId(),bookingDto.getUserId(),bookingDto.getWorkDate())){
            Booking booking = bookingRepository.findByWorkerIdAndUserIdAndWorkDate(bookingDto.getWorkerId(),bookingDto.getUserId(),bookingDto.getWorkDate());
            log.info("booking: {}",booking);
            if(booking.getStatus().equals(Status.CONFIRMED)){
                throw new RuntimeException("You already booked this worker on this date");
            }else if(booking.getStatus().equals(Status.REQUESTED)){
                throw new RuntimeException("You already requested for service on this date");
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
    public BookingResponseDto reScheduleBooking(String bookingId,LocalDate rescheduleDate,String status, Boolean isWorker) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        System.out.println(booking.getStatus()+" :booking status");
        if(booking.getStatus().equals(Status.REQUESTED)){
            booking.setWorkDate(rescheduleDate);
        } else if(booking.getStatus().equals(Status.REQUESTED_FOR_RESCHEDULE)){
            if(status.equals("cancel")){
                booking.setStatus(Status.RESCHEDULE_REQUEST_REJECTED);
            }else{
                booking.setStatus(Status.RESCHEDULED);
            }
        } else if(booking.getStatus().equals(Status.CONFIRMED)) {
            booking.setStatus(Status.REQUESTED_FOR_RESCHEDULE);
            booking.setRescheduleRequestedDate(rescheduleDate);
            if (isWorker) {
                booking.setRescheduleRequestedBy(Role.WORKER);
            } else {
                booking.setRescheduleRequestedBy(Role.USER);
            }
        }
        bookingRepository.save(booking);
        return mapBookingToResponse(booking);
    }
    @Scheduled(cron = "0 43 12 * * ?")
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
    @Override
    public BookingsResponse getAllBookings(Long userId, Long workerId, Integer pageNumber, String status, Date workDate, Long serviceId) {
        Integer pageSize = 6;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Status statusEnum = null;
            if (status != null && !status.isEmpty()) {
                try {
                    statusEnum = Status.valueOf(status.toUpperCase());
                    System.out.println(statusEnum+" : this is status enum");
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid status value: " + status);
                }
            }
        Page<Booking> bookings = bookingRepository.findBookingsByCriteria(workerId,userId,statusEnum,workDate,serviceId,pageable);
        List<BookingResponseDto>  bookingResponseDtos;
        if(bookings != null){
            bookingResponseDtos =    mapBookingsToResponse(bookings);
        }
        else bookingResponseDtos= null;
        log.info("Bookings response : {}",bookingResponseDtos);
        return new BookingsResponse(bookingResponseDtos,bookings.getTotalPages());
    }

    @Override
    public String updateBooking(String bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(Status.valueOf(status));
        bookingRepository.save(booking);
        return "Booking updated successfully";
    }
}
