package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.HousePostSimpleDTO;
import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.UserPostSimpleDTO;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryPostDTO {
    private long id;

    @NotNull(message = "{message.error.notBlank}")
    @Min(value = 0, message = "{message.error.min-price}")
    private double price;

    @NotNull(message = "{message.error.notBlank}")
    @FutureOrPresent(message = "{message.error.date-less}")
    private LocalDate dataBookingStart;

    private LocalDate dataBookingEnd ;

    @NotNull
    @Min(value = 1, message = "{message.error.numDaysBooking}")
    @Max( value = 50, message = "{message.error.numDaysBooking}")
    private long numDaysBooking;

    @NotNull
    @Min(value = 1, message = "{message.error.numTourist}")
    @Max( value = 50, message = "{message.error.numTourist}")
    private int numTourists;

    @NotNull
    private UserPostSimpleDTO user;

    @NotNull
    private HousePostSimpleDTO house;
}
