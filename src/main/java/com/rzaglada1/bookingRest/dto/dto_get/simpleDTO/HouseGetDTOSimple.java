package com.rzaglada1.bookingRest.dto.dto_get.simpleDTO;

import com.rzaglada1.bookingRest.dto.dto_get.AddressGetDto;
import com.rzaglada1.bookingRest.dto.dto_get.FeedbackGetDTO;
import com.rzaglada1.bookingRest.dto.dto_get.ImageGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseGetDTOSimple {
    private long id;

    private String name;
    private String description;
    private int numTourists;
    private double price;
    private Boolean isAvailable;

    private AddressGetDtoSimple address;
    private ImageGetDTOSimple image;


//    private List<FeedbackGetDTOSimple> feedbackList;



}
