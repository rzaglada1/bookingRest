package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.repositories.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;


    public void saveToBase(OrderHistory orderHistory) {
            orderHistoryRepository.save(orderHistory);
    }

    public Page<OrderHistory> findOrdersByUser(User user, Pageable pageable) {
        return orderHistoryRepository.findOrderHistoriesByUser(user, pageable);
    }

    public List<OrderHistory> getOrderHistoryByHouseId (long id) {
        return orderHistoryRepository.getFeedbackByHouseId(id);
    }
}
