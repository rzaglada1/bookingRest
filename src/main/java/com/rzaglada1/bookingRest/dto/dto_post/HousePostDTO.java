package com.rzaglada1.bookingRest.dto.dto_post;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousePostDTO {
    private long id;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =50,  message = "{message.error.length}")
    private String name;
    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =2000,  message = "{message.error.length2000}")
    private String description;

    @NotNull(message = "{message.error.notBlank}")
    @Size(min = 1, max = 50,  message = "{message.error.numTourist}")
    private int numTourists;
    @NotNull(message = "{message.error.notBlank}")
    @Min(value = 0, message = "{message.error.min-price}")
    private double price;

    private Boolean isAvailable;

    private AddressPostDto address;
    private ImagePostDTO image;
    private UserPostDTO user;

//    private List<FeedbackPostDTO> feedbackList;
//    private List<WishPostDTO> wishList;
//    private List<OrderHistoryPostDTO> orderHistoryList;

}
