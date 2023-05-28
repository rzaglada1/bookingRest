package com.rzaglada1.bookingRest.dto.dto_post.simpleDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousePostSimpleDTO {
    @NotNull
    @Min(value = 0)
    private Long id;
}
