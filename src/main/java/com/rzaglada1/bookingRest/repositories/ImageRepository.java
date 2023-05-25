package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.Image;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Cacheable("image")
    @Override
    Optional<Image> findById(Long aLong);
}
