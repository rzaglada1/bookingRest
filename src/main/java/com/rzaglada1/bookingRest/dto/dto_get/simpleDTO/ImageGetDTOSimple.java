package com.rzaglada1.bookingRest.dto.dto_get.simpleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageGetDTOSimple {

    private long id;

    private String name;
    private String fileName;
    private long size;
    private String contentType;
    private byte[] photoToBytes;

}
