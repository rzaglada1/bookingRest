package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.dto.dto_post.HousePostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackGetDTO {

    private long id;
    private double rating;
    private String description;

    private UserPostDTO user;
    private HousePostDTO house;

}
