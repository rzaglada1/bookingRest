package com.rzaglada1.bookingRest.dto.dto_get.simpleDTO;

import com.rzaglada1.bookingRest.dto.dto_get.UserGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackGetDTOSimple {

    private long id;
    private double rating;
    private String description;


}
