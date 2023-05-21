package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.Feedback;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> getFeedbackByHouseId (long houseId);
}
