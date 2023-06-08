package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.WishGetDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.Wish;
import com.rzaglada1.bookingRest.services.HouseService;
import com.rzaglada1.bookingRest.services.UserService;
import com.rzaglada1.bookingRest.services.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
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
        Optional<House> house = houseService.getById(idHouse);
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


    @Operation(summary = "Get wish by house id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "The id house is on the wish list", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Wishes not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping("/{idHouse}")
    public ResponseEntity<WishGetDTO> getWishByHouseId( @Parameter(description = "id of book to be searched")
            @PathVariable Long idHouse
            , Principal principal) {
        Optional<Wish> wish = wishService.getWishByUserAndHouse(idHouse, userService.getUserByPrincipal(principal));
        if (wish.isPresent()) {
            WishGetDTO wishGetDTO = modelMapper.map(wish.get(), WishGetDTO.class);
            return ResponseEntity.ok(wishGetDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @Operation(summary = "Get wishes list by authentication user")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Found wishes list", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            } )
    @GetMapping
    public ResponseEntity<Page<WishGetDTO>> wishesByUser(
            Principal principal
            ,@ParameterObject @PageableDefault(size = 3) Pageable pageable) {
        if (principal != null) {
            Page<Wish> wishPage = wishService.getWishByUser(principal, pageable);
            Page<WishGetDTO> wishGetDTOPage = wishPage.map(objectEntity -> modelMapper.map(objectEntity, WishGetDTO.class));
            return ResponseEntity.ok(wishGetDTOPage);
        }
        return ResponseEntity.badRequest().body(null);
    }


    @Operation(summary = "Wish by house id delete")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Wish deleted", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @DeleteMapping("/{idHouse}")
    public ResponseEntity<?> wishes(
              @PathVariable long idHouse
            , Principal principal
             ) {
        if (houseService.getById(idHouse).isPresent() ) {
            wishService.deleteFromBase(idHouse, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
