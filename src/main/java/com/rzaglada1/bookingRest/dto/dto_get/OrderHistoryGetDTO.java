package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.HouseGetDTOSimple;
import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.UserGetDTOSimple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryGetDTO {
    private long id;

    private double price;

    private LocalDate dataBookingStart;
    private LocalDate dataBookingEnd ;
    private long numDaysBooking;
    private int numTourists;

    private UserGetDTOSimple user;
    private HouseGetDTOSimple house;
}
