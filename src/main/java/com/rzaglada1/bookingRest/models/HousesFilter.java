package com.rzaglada1.bookingRest.models;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class HousesFilter {

    @Length(max = 50, message = "{message.error.length}")
    private String country = "%";
    @Length(max = 50, message = "{message.error.length}")
    private String city = "%";
    @FutureOrPresent(message = "{message.error.date-less}")
    private LocalDate date ;
    @Min(value = 1, message = "{message.error.days}")
    @Max( value = 90, message = "{message.error.days}")
    private Integer days = 1;
    @Min(value = 1, message = "{message.error.numTourists}")
    @Max( value = 50, message = "{message.error.numTourists}")
    private Integer people = 1;

}
