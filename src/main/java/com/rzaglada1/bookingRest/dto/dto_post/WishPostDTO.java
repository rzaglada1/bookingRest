package com.rzaglada1.bookingRest.dto.dto_post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishPostDTO {
    private long id;

    private UserPostDTO user;
    private HousePostDTO house;
}
