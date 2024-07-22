package com.example.booking.controller;

import com.example.booking.model.dto.AvailabilityDateResponse;
import com.example.booking.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/booking/api/v1")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    @GetMapping("/get-availability-dates")
    public ResponseEntity<AvailabilityDateResponse> getAvailabilityDates(@RequestParam Long workerId) {
        AvailabilityDateResponse availabilityDateResponse = availabilityService.getAvailabilityDates(workerId);
        return new ResponseEntity<>(availabilityDateResponse, HttpStatus.OK);
    }
    @PutMapping("/set-availability-dates")
    public ResponseEntity<AvailabilityDateResponse> setAvailabilityDates(@RequestParam long workerId,
                                                                         @RequestBody List<Date> dates){
        return new ResponseEntity<>( availabilityService.setAvailableDates(workerId,dates), HttpStatus.OK);
    }
}
