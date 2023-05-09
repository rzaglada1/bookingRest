package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    Page<OrderHistory> findOrderHistoriesByUser(User user, Pageable pageable);
    List<OrderHistory> findOrderHistoriesByHouse (House house);

}
