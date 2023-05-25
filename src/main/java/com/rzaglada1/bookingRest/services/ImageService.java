package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.Image;
import com.rzaglada1.bookingRest.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;


    public Optional<Image> findById(long id) {
        System.out.println("image1");
        Optional<Image> image = imageRepository.findById(id);
        System.out.println("image2");

        return image;
    }
}
