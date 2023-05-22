package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import com.rzaglada1.bookingRest.repositories.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    public Page<Wish> getWishByUser (Principal principal, Pageable pageable) {
        return wishRepository.getWishByUser(getUserByPrincipal(principal).orElseThrow(), pageable);
    }

    public boolean saveToBase (Wish wish, long houseId, Principal principal) {
        boolean isAllOk = false;
        if (!wishRepository.existsByHouseIdAndUser(houseId, getUserByPrincipal(principal).orElseThrow())) {
            wish.setHouse(houseRepository.findById(houseId).orElseThrow());
            wish.setUser(getUserByPrincipal(principal).orElseThrow());
            wishRepository.save(wish);
            isAllOk = true;
        }
        System.out.println(isAllOk);
        return isAllOk;
    }

    public void deleteFromBase (long houseId, Principal principal){
        if (wishRepository.existsByHouseIdAndUser(houseId, getUserByPrincipal(principal).orElseThrow() )) {
            wishRepository.delete(wishRepository.getWishByHouseIdAndUser(houseId, getUserByPrincipal(principal).orElseThrow()).orElseThrow() );
        }
    }


    public Optional<Wish> getWishByUserAndHouse (long id, User user) {
        return wishRepository.getWishByHouseIdAndUser(id, user);
    }

    private Optional<User> getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }


}
