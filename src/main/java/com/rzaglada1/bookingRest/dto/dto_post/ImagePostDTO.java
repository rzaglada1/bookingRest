package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.HouseGetDTOSimple;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagePostDTO {


    @NotNull(message = "{message.error.notNull-image-name}")
    @Length(min = 1,max =50,  message = "message.error.length-image-name")
    private String name;

    @NotNull(message = "{message.error.notNull-image-fileName}")
    @Length(min = 1, max =50,  message = "{message.error.length-image-fileName}")
    private String fileName;

    @NotNull(message = "{message.error.notNull-image-size}")
    @Max(value = 10485760, message = "{message.error.length-image-size}")
    @Min(value = 1, message = "{message.error.length-image-size}")
    private long size;

    @NotNull(message = "{message.error.notNull-image-content-type}")
    @Length(min = 1,max =50,  message = "{message.error.length-image-content-type}")
    private String contentType;

    @NotEmpty(message = "{message.error.length-image-photoToBytes}")
    private byte[] photoToBytes;

//    @Override
//    public String toString() {
//        return "ImagePostDTO{" +
//                "name='" + name + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", size=" + size +
//                ", contentType='" + contentType + '\'' +
//                '}';
//    }
}
