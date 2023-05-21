package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressPostDto {

    private long id;

    @NotNull(message = "{message.error.notNull-address-country}")
    @Length(min = 1, max = 50, message = "{message.error.length-address-country}")
    private String country;

    @NotNull(message = "{message.error.notNull-address-city}")
    @Length(min = 1, max = 50, message = "{message.error.length-address-city}")
    private String city;

    @NotNull(message = "{message.error.notNull-address-street}")
    @Length(min = 1, max = 50, message = "{message.error.length-address-street}")
    private String street;

    @NotNull(message = "{message.error.notNull-house-number}")
    @Length(min = 1, max = 5, message = "{message.error.length-house-number}")
    private String number;

    @NotNull(message = "{message.error.notNull-house-apartment}")
    private String apartment;

}
