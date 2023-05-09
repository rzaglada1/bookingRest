package com.rzaglada1.bookingRest.dto.dto_get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishGetDTO {
    private long id;

    private UserGetDTO user;
    private HouseGetDTO house;
}
