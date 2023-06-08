package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.OrderHistoryGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.OrderHistoryPostDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.services.HouseService;
import com.rzaglada1.bookingRest.services.OrderHistoryService;
import com.rzaglada1.bookingRest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springdoc.core.annotations.ParameterObject;
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


    @Operation(summary = "Get order history by house id (for the availability calendar)")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Orders history by house id", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Orders history not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping("/{idHouse}")
    public ResponseEntity<List<OrderHistoryGetDTO>> getOrderHistoryByHouseId(
            @PathVariable Long idHouse
    ) {
        ModelMapper modelMapper = new ModelMapper();
        List<OrderHistory> orderHistoryList = orderHistoryService.getOrderHistoryByHouseId(idHouse);

        Type listType = new TypeToken<List<OrderHistoryGetDTO>>() {
        }.getType();
        List<OrderHistoryGetDTO> orderHistoryGetDTOList = modelMapper.map(orderHistoryList, listType);
        return ResponseEntity.ok(orderHistoryGetDTOList);
    }


    @Operation(summary = "Get orders history list by authentication user")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Found orders list", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping
    public ResponseEntity<Page<OrderHistoryGetDTO>> wishesByUser(
            Principal principal
            ,@ParameterObject @PageableDefault(size = 3) Pageable pageable) {
        if (principal != null) {
            Page<OrderHistory> historyPage = orderHistoryService.findOrdersByUser(userService.getUserByPrincipal(principal), pageable);
            Page<OrderHistoryGetDTO> historyGetDTOPage = historyPage.map(objectEntity -> modelMapper.map(objectEntity, OrderHistoryGetDTO.class));
            return ResponseEntity.ok(historyGetDTOPage);
        }
        return ResponseEntity.badRequest().body(null);
    }


    @Operation(summary = "Booking by house id. if prebooking is present, only the check of the availability of the house according to the specified parameters")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "If response body not null then House available for booking", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content),
                    @ApiResponse(responseCode = "404", description = "House not found", content = @Content)
            })

    @PostMapping({"/{idHouse}", "/{idHouse}/{prebooking}"})
    public ResponseEntity<?> housePreBooking(
            @PathVariable("idHouse") long idHouse
            , @PathVariable(value = "prebooking", required = false) String prebooking
            , @RequestBody @Valid OrderHistoryPostDTO orderHistoryPostDTO
            , BindingResult bindingResult
            , Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        OrderHistory orderHistory = new OrderHistory();
        User user = userService.getUserByPrincipal(principal);

        orderHistory.setDataBookingStart(orderHistoryPostDTO.getDataBookingStart());
        orderHistory.setNumDaysBooking(orderHistoryPostDTO.getNumDaysBooking());
        orderHistory.setNumTourists(orderHistoryPostDTO.getNumTourists());
        orderHistory.setDataBookingEnd(orderHistory.getDataBookingStart().plusDays(orderHistory.getNumDaysBooking()));
        orderHistory.setUser(user);

        House house = houseService.getById(idHouse).orElseThrow();
        if (houseService.isDateFree(orderHistory, idHouse) && house.getNumTourists() >= orderHistory.getNumTourists()) {
            orderHistory.setHouse(house);
            if (prebooking == null) {
                orderHistoryService.saveToBase(orderHistory);
            }

            OrderHistoryGetDTO orderHistoryGetDTO = modelMapper.map(orderHistory, OrderHistoryGetDTO.class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(orderHistoryGetDTO);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Map<String, String> mapErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f -> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }


}
