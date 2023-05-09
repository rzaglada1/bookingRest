package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressPostDto {

    private long id;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max = 50, message = "{message.error.length}")
    private String country;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max = 50, message = "{message.error.length}")
    private String city;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max = 50, message = "{message.error.length}")
    private String street;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max = 50, message = "{message.error.length}")
    private String number;

    private String apartment;

    private HouseGetDTO house;



}
