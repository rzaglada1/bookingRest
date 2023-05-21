package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.HousePostSimpleDTO;
import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.UserPostSimpleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishPostDTO {
    private long id;

    @NotNull
    @Valid
    private UserPostSimpleDTO user;

    @NotNull
    @Valid
    private HousePostSimpleDTO house;
}
