package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.models.Image;
import com.rzaglada1.bookingRest.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get image by id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Got image by id",
                            content = @Content(schema = @Schema(implementation = ByteArrayInputStream.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Image not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content)
            })

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById (@PathVariable long id) {
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Optional<Image> image = imageService.findById(id);
        if (image.isPresent()) {
            responseEntity = ResponseEntity.ok()
                    .cacheControl(cacheControl)
                    .contentType(MediaType.valueOf(image.get().getContentType()))
                    .contentLength(image.get().getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.get().getPhotoToBytes())));
        }
        return responseEntity;
    }

}
