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

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    @Override
    public Page<Booking> findBookingsByWorkerIdAndCriteria(Long workerId, Status status, Date workDate, Long serviceId, Pageable pageable) {
        Criteria criteria =  Criteria.where("workerId").is(workerId);
        if(status != null){
            criteria.and("status").is(status);
        }
        if(workDate != null){
            criteria.and("workDate").is(workDate);
        }
        if(serviceId != null){
            criteria.and("serviceId").is(serviceId);
        }
        Query query = new Query(criteria).with(pageable);
        long total = mongoTemplate.count(query,Booking.class);
        return PageableExecutionUtils.getPage(mongoTemplate.find(query,Booking.class),pageable,()->total);
    }
}
