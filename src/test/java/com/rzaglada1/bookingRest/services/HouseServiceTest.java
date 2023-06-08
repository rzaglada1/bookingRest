package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class HouseServiceTest {

    @Mock
    HouseRepository houseRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void getHouseById() {

        HouseService houseService = new HouseService(houseRepository,userRepository);
        House house = new House();
        house.setId(1);
        Optional<House> houseOptionalRepo = Optional.of(house);
        when(houseRepository.findById(1L)).thenReturn(houseOptionalRepo);

        Optional<House> houseOptional = houseService.getById(1);

        assertEquals(houseOptionalRepo, houseOptional);
    }


    @Test
    void getAllTest() {
        HouseService houseService = new HouseService(houseRepository,userRepository);
        List<House> houses = new ArrayList<>();
        House house1 = new House();
        House house2 = new House();
        House house3 = new House();
        house1.setId(1);
        house2.setId(2);
        house3.setId(3);
        houses.add(house1);
        houses.add(house2);
        houses.add(house3);

        Pageable pageable= PageRequest.of(0,3);

        Page<House> housePageRepo = new PageImpl<>(houses,pageable,0);

        when(houseRepository.findAll(pageable)).thenReturn(housePageRepo);

        Page<House> housePage = houseService.getAll(pageable);

        assertEquals(housePageRepo, housePage);
    }


    @Test
    void filterHousesNotFoundObjectsTest() {
        HouseService houseService = new HouseService(houseRepository,userRepository);
        List<House> houses = new ArrayList<>();
        House house1 = new House();
        House house2 = new House();
        House house3 = new House();
        house1.setId(1);
        house2.setId(2);
        house3.setId(3);
        houses.add(house1);
        houses.add(house2);
        houses.add(house3);

        Pageable pageable= PageRequest.of(0,3);
        String country = "Ukraine";
        String city = "%";
        LocalDate localDate = LocalDate.now();
        int people = 1;
        int days = 1;

        Page<House> housePageRepo = new PageImpl<>(houses,pageable,0);

        when(houseRepository.getHouseByFilter(country, city, localDate, localDate, people, pageable)).thenReturn(housePageRepo);

        Page<House> housePage = houseService.filterHouses(country, city, localDate, days, people, pageable);

        assertEquals(housePageRepo, housePage);

    }
}