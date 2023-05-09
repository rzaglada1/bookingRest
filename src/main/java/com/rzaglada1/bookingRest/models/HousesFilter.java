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
    private String country;
    @Length(max = 50, message = "{message.error.length}")
    private String city;
    @FutureOrPresent(message = "Дата менша за поточну")
    private LocalDate date;
    @Min(value = 1, message = "{message.error.min-filter-days}")
    @Max( value = 50, message = "{message.error.max-filter-days}")
    private Integer days;
    @Min(value = 1, message = "{message.error.min-filter-people}")
    @Max( value = 50, message = "{message.error.max-filter-people}")
    private Integer people = 2;

}
