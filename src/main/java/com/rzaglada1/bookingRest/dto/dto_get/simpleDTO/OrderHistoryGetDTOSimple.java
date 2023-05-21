package com.rzaglada1.bookingRest.dto.dto_get.simpleDTO;

import com.rzaglada1.bookingRest.dto.dto_get.UserGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryGetDTOSimple {
    private long id;

    private double price;

    private LocalDate dataBookingStart;
    private LocalDate dataBookingEnd ;
    private long numDaysBooking;
    private int numTourists;

}
