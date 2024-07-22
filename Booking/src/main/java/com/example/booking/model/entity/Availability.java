package com.example.booking.model.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "availability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Availability {
    @Id
    private String id;
    private Long workerId;
    private List<Date> notAvailableDates;
    public void addNotAvailableDates(List<Date> dates) {
        if (dates != null && !dates.isEmpty()) {
            if (this.notAvailableDates == null) {
                this.notAvailableDates = new ArrayList<>();
            }
            this.notAvailableDates.addAll(dates);
        }
    }
}
