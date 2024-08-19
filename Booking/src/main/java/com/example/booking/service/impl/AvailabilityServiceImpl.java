package com.example.booking.service.impl;
import com.example.booking.model.dto.AvailabilityDateResponse;
import com.example.booking.model.entity.Availability;
import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import com.example.booking.repository.AvailabilityRepository;
import com.example.booking.repository.BookingRepository;
import com.example.booking.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService
{
    private final BookingRepository bookingRepository;
    private final AvailabilityRepository availabilityRepository;
    @Override
    public AvailabilityDateResponse getAvailabilityDates(Long workerId) {
        List<Booking> bookings = bookingRepository.findByWorkerIdAndStatus(workerId, Status.CONFIRMED);
        List<LocalDate> bookingDates = new ArrayList<>();
        bookings.forEach(booking -> bookingDates.add(booking.getWorkDate()));
        Availability availability = availabilityRepository.findByWorkerId(workerId);
        AvailabilityDateResponse availabilityDateResponse = new AvailabilityDateResponse();
        availabilityDateResponse.setBookedDates(bookingDates);
        if(availability != null){
            availabilityDateResponse.setUnavailableDates(availability.getNotAvailableDates());
        }
        return availabilityDateResponse;
    }

    @Override
    public AvailabilityDateResponse setAvailableDates(long workerId, List<Date> dates) {
        Availability availability = availabilityRepository.existsByWorkerId(workerId) ?
                availabilityRepository.findByWorkerId(workerId) :
                new Availability();
        availability.addNotAvailableDates(dates);
        availability.setWorkerId(workerId);
        availabilityRepository.save(availability);
        List<Booking> bookings = bookingRepository.findByWorkerIdAndStatus(workerId,Status.CONFIRMED);
        AvailabilityDateResponse availabilityDateResponse = new AvailabilityDateResponse();
        availabilityDateResponse.setBookedDates(bookings.stream().map(booking ->  booking.getWorkDate()).collect(Collectors.toList()));
        if(availability!=null)
            availabilityDateResponse.setUnavailableDates(availability.getNotAvailableDates());
        System.out.println(availabilityDateResponse.getBookedDates());
        System.out.println(availabilityDateResponse.getUnavailableDates());
        return availabilityDateResponse;
    }
}
