package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.WishGetDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import com.rzaglada1.bookingRest.services.HouseService;
import com.rzaglada1.bookingRest.services.UserService;
import com.rzaglada1.bookingRest.services.WishService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;
    private final UserService userService;
    private final HouseService houseService;
    private final ModelMapper modelMapper = new ModelMapper();


    // new wish
    @PostMapping("/{idHouse}")
    public ResponseEntity<?> createWish(
            @PathVariable long idHouse
            , Principal principal
    ) {
        Optional<House> house = houseService.getHouseById(idHouse);
        User user = userService.getUserByPrincipal(principal);
        Wish wish = new Wish();
        if (house.isPresent()  ) {
            wish.setUser(user);
            wish.setHouse(house.get());
            if (wishService.saveToBase(wish, idHouse, principal) ) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/{id}")
    public ResponseEntity<WishGetDTO> getWishByHouseId(
            @PathVariable Long id
            , Principal principal) {
        Optional<Wish> wish = wishService.getWishByUserAndHouse(id, userService.getUserByPrincipal(principal));
        if (wish.isPresent()) {
            WishGetDTO wishGetDTO = modelMapper.map(wish.get(), WishGetDTO.class);
            return ResponseEntity.ok(wishGetDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @GetMapping
    public ResponseEntity<Page<WishGetDTO>> wishesByUser(
            Principal principal
            , @PageableDefault(size = 3) Pageable pageable) {
        Page<Wish> wishPage = wishService.getWishByUser(principal, pageable);
        Page<WishGetDTO> wishGetDTOPage = wishPage.map(objectEntity -> modelMapper.map(objectEntity, WishGetDTO.class));
        return ResponseEntity.ok(wishGetDTOPage);
    }


    @DeleteMapping("/{houseId}")
    public ResponseEntity<?> wishes(
              @PathVariable long houseId
            , Principal principal
             ) {
        if (houseService.getHouseById(houseId).isPresent() ) {
            wishService.deleteFromBase(houseId, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
