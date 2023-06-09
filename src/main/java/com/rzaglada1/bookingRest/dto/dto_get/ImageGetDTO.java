package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.dto.dto_get.simpleDTO.HouseGetDTOSimple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageGetDTO {

    private long id;

    private String name;
    private String fileName;
    private long size;
    private String contentType;
    private byte[] photoToBytes;

    private HouseGetDTOSimple house;

}
