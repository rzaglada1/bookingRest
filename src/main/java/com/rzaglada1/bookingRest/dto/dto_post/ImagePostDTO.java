package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagePostDTO {

    private long id;

    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =50,  message = "{message.error.length}")
    private String name;
    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =50,  message = "{message.error.length}")
    private String fileName;
    @Size(max = 10485760, message = "{message.error.size-image}")
    private long size;
    @NotBlank(message = "{message.error.notBlank}")
    @Length(max =50,  message = "{message.error.length}")
    private String contentType;
    @Size(max = 10485760, message = "{message.error.size-image}")
    private byte[] photoToBytes;

    private HouseGetDTO house;
}
