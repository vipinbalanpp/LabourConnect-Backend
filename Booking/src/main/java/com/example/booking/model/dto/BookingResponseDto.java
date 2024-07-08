package com.example.booking.model.dto;
import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class
BookingResponseDto {
    public BookingResponseDto(Booking booking){
        this.id = booking.getId();
        this.workDescription = booking.getWorkDescription();
        this.bookingDate = booking.getBookingDate();
        this.workDate = booking.getWorkDate();
        this.status = booking.getStatus();
        this.serviceCharge = booking.getServiceCharge();
        this.cancellationReason = booking.getCancellationReason();
        this.cancelledBy = booking.getCancelledBy();
        this.workLocationAddress = new AddressDto(booking.getWorkLocationAddress());
    }
    private String id;
    private UserDto user;
    private WorkerDto worker;
    private String workDescription;
    private Date bookingDate;
    private Date workDate;
    private Status status;
    private Double serviceCharge;
    private String cancellationReason;
    private String cancelledBy;
    private AddressDto workLocationAddress;
}
