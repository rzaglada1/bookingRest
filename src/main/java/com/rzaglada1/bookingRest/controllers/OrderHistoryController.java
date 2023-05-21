package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.OrderHistoryGetDTO;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.services.OrderHistoryService;
import com.rzaglada1.bookingRest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderHistoryGetDTO>> getOrderHistoryByHouseId (
            @PathVariable Long id
            ) {
        ModelMapper modelMapper = new ModelMapper();
        List<OrderHistory> orderHistoryList = orderHistoryService.getOrderHistoryByHouseId(id);

        Type listType = new TypeToken<List<OrderHistoryGetDTO>>(){}.getType();
        List<OrderHistoryGetDTO> orderHistoryGetDTOList = modelMapper.map(orderHistoryList,listType);
        return ResponseEntity.ok(orderHistoryGetDTOList);
    }


    @GetMapping
    public ResponseEntity<Page<OrderHistoryGetDTO>> wishesByUser(
            Principal principal
            , @PageableDefault(size = 3) Pageable pageable) {
        Page<OrderHistory> historyPage = orderHistoryService.findOrdersByUser(userService.getUserByPrincipal(principal), pageable);
        Page<OrderHistoryGetDTO> historyGetDTOPage = historyPage.map(objectEntity -> modelMapper.map(objectEntity, OrderHistoryGetDTO.class));
        return ResponseEntity.ok(historyGetDTOPage);
    }


}
