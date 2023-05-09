package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> getWishByUser (User user, Pageable pageable);

    boolean existsByHouseIdAndUser (long idHouse, User user);
    Wish getWishByHouseIdAndUser (long houseId, User user);
}
