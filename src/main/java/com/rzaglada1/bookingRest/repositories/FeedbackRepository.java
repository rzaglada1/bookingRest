package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> getFeedbackByHouseId (long houseId);
}
