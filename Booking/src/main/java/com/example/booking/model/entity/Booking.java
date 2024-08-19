package com.example.booking.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    private String id;
    private Long userId;
    private Long workerId;
    private String workDescription;
    private LocalDate bookingDate;
    private LocalDate workDate;
    private Status status;
    private Double serviceCharge;
    private String reasonForRejection;
    private String cancellationReason;
    private String cancelledBy;
    private Address workLocationAddress;
}
