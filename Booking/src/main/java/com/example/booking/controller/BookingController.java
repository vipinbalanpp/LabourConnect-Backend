package com.example.booking.controller;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.model.dto.BookingsResponse;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity< BookingsResponse> getAllBookings (@RequestParam (required = false) Long userId,
                                                                   @RequestParam (required = false)Long workerId,
                                                             @RequestParam Integer pageNumber) {
        System.out.println(userId);
        System.out.println(pageNumber);
       if(userId != null)return new ResponseEntity<>(bookingService.getAllBookingsOfUser(userId,pageNumber),HttpStatus.OK);
       else if(workerId != null)return  new ResponseEntity<>(bookingService.getAllBookingsOfWorker(workerId,pageNumber),HttpStatus.OK);
       else throw new RuntimeException("Something went wrong while fetching bookings");

    }
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> requestReschedule(@PathVariable String bookingId,
                                                                @RequestParam boolean isWorker){

        BookingResponseDto bookingResponseDto = bookingService.reScheduleBooking(bookingId,isWorker);
        return new ResponseEntity<>(bookingResponseDto,HttpStatus.OK);
    }
    @PutMapping("/update")
    public String updateBooking(){
        return "updated";
    }

}
