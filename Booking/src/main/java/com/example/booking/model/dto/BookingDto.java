package com.example.booking.model.dto;

import com.example.booking.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private String id;
    private Long userId;
    private Long workerId;
    private String workDescription;
    private LocalDate bookingDate;
    private LocalDate workDate;
    private String status;
    private Double serviceCharge;
    private String cancellationReason;
    private String cancelledBy;
    private Address workAddress;


}
