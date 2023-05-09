package com.rzaglada1.bookingRest.dto.dto_get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressGetDto {

    private long id;

    private String country;
    private String city;
    private String street;
    private String number;
    private String apartment;

    private HouseGetDTO house;
}
