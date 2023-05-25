package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.models.Image;
import com.rzaglada1.bookingRest.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping(value = "/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById (@PathVariable long id) {
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Optional<Image> image = imageService.findById(id);
        if (image.isPresent()) {
            responseEntity = ResponseEntity.ok()
//                    .header("fileName", image.get().getFileName())
                    .contentType(MediaType.valueOf(image.get().getContentType()))
                    .contentLength(image.get().getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.get().getPhotoToBytes())));
        }
        return responseEntity;
    }

}
