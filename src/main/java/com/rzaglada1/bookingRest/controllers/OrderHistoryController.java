package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.OrderHistoryGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.OrderHistoryPostDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.services.HouseService;
import com.rzaglada1.bookingRest.services.OrderHistoryService;
import com.rzaglada1.bookingRest.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;
    private final UserService userService;
    private final HouseService houseService;
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

    @PostMapping({"/{houseId}", "/{houseId}/{prebooking}"})
    public ResponseEntity<?> housePreBooking(
            @PathVariable("houseId") long houseId
            , @PathVariable(value = "prebooking", required = false) String prebooking
            ,@RequestBody @Valid OrderHistoryPostDTO orderHistoryPostDTO
            , BindingResult bindingResult
            , Principal principal
    ) {
        if (bindingResult.hasErrors() ) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        OrderHistory orderHistory = modelMapper.map(orderHistoryPostDTO, OrderHistory.class);



        orderHistory.setDataBookingEnd(orderHistory.getDataBookingStart().plusDays(orderHistory.getNumDaysBooking()));

        House house = houseService.getHouseById(houseId).orElseThrow();
        if (houseService.isDateFree(orderHistory, houseId) && house.getNumTourists() >= orderHistory.getNumTourists()) {
            orderHistory.setHouse(house);
            if (prebooking == null) {
                orderHistoryService.saveToBase(orderHistory, houseId, principal);
            }


            OrderHistoryGetDTO orderHistoryGetDTO = modelMapper.map(orderHistory, OrderHistoryGetDTO.class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(orderHistoryGetDTO);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Map<String, String> mapErrors (BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f-> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }



}
