package com.example.booking.repository.impl;

import com.example.booking.model.entity.Booking;
import com.example.booking.model.entity.Status;
import com.example.booking.repository.BookingRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    @Override
    public Page<Booking> findBookingsByCriteria(Long workerId, Long userId, Status status, Date workDate, Long serviceId, Pageable pageable) {
        // Define Criteria as usual
        Criteria criteria = new Criteria();
        if (workerId != null) {
            criteria.and("workerId").is(workerId);
        }
        if (userId != null) {
            criteria.and("userId").is(userId);
        }
        if (status != null) {
            criteria.and("status").is(status);
        }
        if (workDate != null) {
            LocalDate localDate = workDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date startDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            criteria.and("workDate").gte(startDate).lt(endDate);
        }
        if (serviceId != null) {
            criteria.and("serviceId").is(serviceId);
        }
        Query queryWithPagination = new Query(criteria).with(pageable);
        Query countQuery = new Query(criteria);
        long total = mongoTemplate.count(countQuery, Booking.class);
        List<Booking> bookings = mongoTemplate.find(queryWithPagination, Booking.class);
        return PageableExecutionUtils.getPage(bookings, pageable, () -> total);
    }


}
