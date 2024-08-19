package com.example.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDateResponse {
    private List<LocalDate> bookedDates;
    private  List<Date> unavailableDates;
}
