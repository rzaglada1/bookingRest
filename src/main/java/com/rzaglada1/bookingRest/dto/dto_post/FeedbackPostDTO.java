package com.rzaglada1.bookingRest.dto.dto_post;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FeedbackPostDTO {

    private long id;
    @NotNull( message = "{message.error.notBlank}" )
    @Min(value = 1, message = "{message.error.mark}")
    @Max( value = 10, message = "{message.error.mark}")
    private double rating;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =2000,  message = "{message.error.length2000}")
    private String description;

    private UserPostDTO user;
    private HousePostDTO house;

}
