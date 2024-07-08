package com.example.booking.service;

import com.example.booking.model.dto.AvailabilityDateResponse;

import java.util.Date;
import java.util.List;

public interface AvailabilityService {
    AvailabilityDateResponse getAvailabilityDates(Long workerId);

    AvailabilityDateResponse setAvailableDates(long workerId, List<Date> dates);
}
