package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
