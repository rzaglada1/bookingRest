package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.repositories.OrderHistoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderHistoryServiceTest {

    @Mock
    OrderHistoryRepository orderHistoryRepository;

    @Test
    void findOrdersByUserTest() {
        Pageable pageable= PageRequest.of(0,3);
        OrderHistoryService orderHistoryService = new OrderHistoryService(orderHistoryRepository);
        User user = new User();
        user.setId(1);

        House house = new House();
        house.setId(1);
        OrderHistory orderHistory = OrderHistory.builder().house(house).user(user).build();
        List<OrderHistory> orderHistoryList = List.of(orderHistory);
        Page<OrderHistory> orderHistoryPageRepo = new PageImpl<>(orderHistoryList,pageable,0);

        when(orderHistoryRepository.findOrderHistoriesByUser(user, pageable)).thenReturn(orderHistoryPageRepo);

        Page<OrderHistory> actualList = orderHistoryService.findOrdersByUser(user, pageable);

        assertEquals(orderHistoryPageRepo, actualList);
    }

    @Test
    void getOrderHistoryByHouseIdTest () {
        OrderHistoryService orderHistoryService = new OrderHistoryService(orderHistoryRepository);

        House house = new House();
        house.setId(1);
        OrderHistory orderHistory = OrderHistory.builder().house(house).build();
        List<OrderHistory> orderHistoryList = List.of(orderHistory);

        when(orderHistoryRepository.getOrderHistoryByHouseId(1)).thenReturn(orderHistoryList);

        List<OrderHistory> actualList = orderHistoryService.getOrderHistoryByHouseId(1);

        assertEquals(orderHistoryList, actualList);
    }

}