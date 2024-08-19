package com.example.booking.controller;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.BookingsResponse;
import com.example.booking.model.entity.Status;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/booking/api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    @PostMapping()
    public String createBooking(@RequestBody BookingDto bookingDto) {
        bookingService.createBooking(bookingDto);
        return "Service booked successfully";
    }

    @GetMapping()
    public ResponseEntity<BookingsResponse> getAllBookings(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long workerId,
            @RequestParam Integer pageNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date workDate,
            @RequestParam(required = false) Long serviceId) {

        System.out.println("--------------<>---------");
        System.out.println("UserId: " + userId);
        System.out.println("WorkerId: " + workerId);
        System.out.println("PageNumber: " + pageNumber);
        System.out.println("Status: " + status);
        System.out.println("WorkDate: " + workDate);
        System.out.println("ServiceId: " + serviceId);
        System.out.println("----------------<>-----------");

        if (userId != null) {
            return new ResponseEntity<>(bookingService.getAllBookingsOfUser(userId, pageNumber), HttpStatus.OK);
        } else if (workerId != null) {
            Status statusEnum = null;
            if (status != null && !status.isEmpty()) {
                try {
                    statusEnum = Status.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid status value: " + status);
                }
            }
            return new ResponseEntity<>(bookingService.getAllBookingsOfWorker(workerId, statusEnum, workDate, serviceId, pageNumber), HttpStatus.OK);
        } else {
            throw new RuntimeException("Something went wrong while fetching bookings");
        }
    }


    @PutMapping("/update")
    public String updateBooking(){
        return "updated";
    }
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable String bookingId,
                                                            @RequestParam String cancellationReason,
                                                            @RequestParam String cancelledBy){
        try {
          BookingResponseDto bookingResponseDto =  bookingService.cancelBooking(bookingId,cancellationReason,cancelledBy);
            System.out.println(cancellationReason);
            return new ResponseEntity<>(bookingResponseDto,HttpStatus.OK);
        }catch (Exception e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<String> confirmBooking(@PathVariable String bookingId){
        try {
            System.out.println(bookingId);
            bookingService.confirmBooking(bookingId);
            return new ResponseEntity<>("booking confirmed successfully",HttpStatus.OK);
        }catch (Exception e){
            throw  new RuntimeException("Something went wrong");
        }
    }
    @PutMapping("/reject/{bookingId}")
    public ResponseEntity<String> rejectBooking(@PathVariable String bookingId,
                                                @RequestParam String reasonForRejection){
        try {
            System.out.println(bookingId);
            System.out.println(reasonForRejection);
            bookingService.rejectBooking(bookingId,reasonForRejection);
            return new ResponseEntity<>("booking rejected successfully",HttpStatus.OK);
        }catch (Exception e){
            throw  new RuntimeException("Something went wrong");
        }
    }

}
