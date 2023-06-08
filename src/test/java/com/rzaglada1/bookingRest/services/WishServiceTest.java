package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import com.rzaglada1.bookingRest.repositories.WishRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class WishServiceTest {

    @Mock
    WishRepository wishRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    HouseRepository houseRepository;

    @Test
    void getWishByUserIdAndHouseTestMock() {
        WishService wishService= new WishService(wishRepository, userRepository, houseRepository);
        User user = new User();
        user.setId(1);
        House house = new House();
        house.setId(1);

        Wish wish = Wish.builder().user(user).house(house).build();
        Optional<Wish> wishOptionalExpect = Optional.of(wish);

        when(wishRepository.getWishByHouseIdAndUser(1, user)).thenReturn(wishOptionalExpect);

        Optional<Wish> wishOptionalActual = wishService.getWishByUserAndHouse(1, user);

        assertEquals(wishOptionalExpect.orElseThrow(), wishOptionalActual.orElseThrow());
    }

}