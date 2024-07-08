package com.example.booking.controller;
import com.example.booking.client.UserServiceClient;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity< List<BookingResponseDto>> getAllBookings (){
       return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> requestReschedule(@PathVariable String bookingId,
                                                                @RequestParam boolean isWorker){
        BookingResponseDto bookingResponseDto = bookingService.reScheduleBooking(bookingId,isWorker);
        return new ResponseEntity<>(bookingResponseDto,HttpStatus.OK);
    }
}
