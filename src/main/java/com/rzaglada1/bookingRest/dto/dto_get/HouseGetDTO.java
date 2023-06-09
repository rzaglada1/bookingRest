package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseGetDTO {
    private long id;

    private String name;
    private String description;
    private int numTourists;
    private double price;
    private Boolean isAvailable;

    private AddressGetDtoSimple address;
    private ImageGetDTOSimple image;
    private UserGetDTOSimple user;

    private List<FeedbackGetDTOSimple> feedbackList;
    private List<WishGetDTOSimple> wishList;
    private List<OrderHistoryGetDTOSimple> orderHistoryList;
}
