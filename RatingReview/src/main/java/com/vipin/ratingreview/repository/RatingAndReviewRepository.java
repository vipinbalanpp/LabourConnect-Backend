package com.vipin.ratingreview.repository;

import com.vipin.ratingreview.model.entity.RatingAndReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingAndReviewRepository extends JpaRepository<RatingAndReview,Long> {
}
