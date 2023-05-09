package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.dto.dto_post.FeedbackPostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.OrderHistoryPostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.WishPostDTO;
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

    private AddressGetDto address;
    private ImageGetDTO image;
    private UserPostDTO user;

    private List<FeedbackPostDTO> feedbackList;
    private List<WishPostDTO> wishList;
    private List<OrderHistoryPostDTO> orderHistoryList;
}
