package com.rzaglada1.bookingRest.dto.dto_post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousePostDTO {
    private long id;

    @NotNull(message = "{message.error.notNull-house-name}")
    @Length(min = 1, max =50,  message = "{message.error.length-house-name}")
    private String name;
    @NotNull(message = "{message.error.notNull-house-description}")
    @Length(min = 1, max =2000,  message = "{message.error.length-house-description}")
    private String description;

    @NotNull(message = "{message.error.notNull-house-tourist}")
    @Min(value = 1, message = "{message.error.min-house-tourist}")
    @Max( value = 50, message = "{message.error.max-house-tourist}")
    private int numTourists;
    @NotNull(message = "{message.error.notNull-house-price}")
    @Min(value = 0, message = "{message.error.positive-house-price}")
    private double price;

    @NotNull(message = "{message.error.notNull-house-isAvailable}")
    private Boolean isAvailable;

    @NotNull(message = "{message.error.notNull-house-address}")
    @Valid
    private AddressPostDto address;

    @Valid
    private ImagePostDTO image;
}
