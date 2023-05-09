package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
