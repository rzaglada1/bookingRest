package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.*;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HouseService {
    private final HouseRepository repositoryHouse;
    private final UserRepository repositoryUser;


    public void saveToBase(Principal principal, House house) throws IOException {
//        // set image
        if (house.getImage() != null && house.getImage().getSize() != 0) {
            house.getImage().setHouse(house);
        }

        // set User
        house.setUser(getUserByPrincipal(principal));

//        // set address
        house.getAddress().setHouse(house);

        // clear spase in description
        house.setDescription(house.getDescription().trim());
        repositoryHouse.save(house);
    }


    @CacheEvict(value="image", allEntries=true)
    public void update(House house, long id) throws IOException {
        Optional<House> houseOptional = getById(id);
        if (houseOptional.isPresent()) {
            House houseUpdate = houseOptional.get();

            Address addressUpdate = houseUpdate.getAddress();
            Image imageUpdate;
            if (houseUpdate.getImage() == null) {
                imageUpdate = new Image();
                imageUpdate.setHouse(houseUpdate);
            } else {
                imageUpdate = houseUpdate.getImage();
            }

            houseUpdate.setName(house.getName());
            houseUpdate.setDescription(house.getDescription().trim());
            houseUpdate.setPrice(house.getPrice());
            houseUpdate.setIsAvailable(house.getIsAvailable());
            houseUpdate.setNumTourists(house.getNumTourists());

            addressUpdate.setCountry(house.getAddress().getCountry());
            addressUpdate.setCity(house.getAddress().getCity());
            addressUpdate.setStreet(house.getAddress().getStreet());
            addressUpdate.setNumber(house.getAddress().getNumber());
            addressUpdate.setApartment(house.getAddress().getApartment());


            // set image
            if (house.getImage() != null && house.getImage().getSize() != 0) {
                imageUpdate.setName(house.getImage().getName());
                imageUpdate.setFileName(house.getImage().getFileName());
                imageUpdate.setContentType(house.getImage().getContentType());
                imageUpdate.setSize(house.getImage().getSize());
                imageUpdate.setPhotoToBytes(house.getImage().getPhotoToBytes());
                houseUpdate.setImage(imageUpdate);
            }
            // set address
            houseUpdate.setAddress(addressUpdate);

            repositoryHouse.save(houseUpdate);
        }
    }

    //==============================================



    public Page<House> getHouseByUser(User user, Pageable pageable) {
        return repositoryHouse.getHouseByUser(user, pageable);
    }


    private User getUserByPrincipal(Principal principal) {
        return repositoryUser.findByEmail(principal.getName()).orElseThrow();
    }



    public void deleteById(long id) {
        repositoryHouse.delete(repositoryHouse.getReferenceById(id));
    }

    public Page<House> getAll(Pageable pageable) {
        return repositoryHouse.findAll(pageable);
    }

    public Optional<House> getById(long id) {
        return repositoryHouse.findById(id);
    }





    public Page<House> filterHouses(String country,
                                    String city,
                                    LocalDate dateBookingStart,
                                    int days,
                                    int people,
                                    Pageable pageable
    ) {
        LocalDate dateBookingEnd = dateBookingStart.plusDays(days);
        return repositoryHouse.getHouseByFilter(country,
                city,
                dateBookingStart,
                dateBookingEnd.minusDays(1),
                people,
                pageable
        );
    }



    public boolean isDateFree(OrderHistory orderHistory, long houseId) {
        LocalDate getDataBookingStart = orderHistory.getDataBookingStart();
        LocalDate getDataBookingEnd = orderHistory.getDataBookingEnd();
        return repositoryHouse.
                getHouseByDate(getDataBookingStart, getDataBookingEnd.minusDays(1), houseId).size() == 0;
    }
}
