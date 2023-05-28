package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_post.simpleDTO.HousePostSimpleDTO;
import com.rzaglada1.bookingRest.dto.dto_post.simpleDTO.UserPostSimpleDTO;
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
//    private long id;
//
//    @NotNull(message = "{message.error.notNull-history-price}")
//    @Min(value = 0, message = "{message.error.min-history-price}")
//    private double price;

    @NotNull(message = "{message.error.notNull-history-bookingStart}")
    @FutureOrPresent(message = "{message.error.future-history-bookingStart}")
    private LocalDate dataBookingStart;

//    @NotNull(message = "{message.error.notNull-history-bookingEnd}")
//    @FutureOrPresent(message = "{message.error.future-history-bookingEnd}")
//    private LocalDate dataBookingEnd ;

    @NotNull(message = "{message.error.notNull-history-days}")
    @Min(value = 1, message = "{message.error.numDaysBooking}")
    @Max( value = 50, message = "{message.error.numDaysBooking}")
    private long numDaysBooking;

    @NotNull
    @Min(value = 1, message = "{message.error.min-history-days}")
    @Max( value = 50, message = "{message.error.max-history-days}")
    private int numTourists;

    @NotNull(message = "{message.error.notNull-history-user}")
    private UserPostSimpleDTO user;

    @NotNull(message = "{message.error.notNull-history-house}")
    private HousePostSimpleDTO house;
}
