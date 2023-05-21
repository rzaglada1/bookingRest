package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.HousePostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.OrderHistoryPostDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.OrderHistory;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final UserService userService;
    private final WishService wishService;
    private final OrderHistoryService orderHistoryService;


    private final ModelMapper modelMapper = new ModelMapper();


    // request form all  house
    @GetMapping
    public ResponseEntity<Page<HouseGetDTO>>  houseAll(
            Principal principal
            , @PageableDefault( sort = {"id"}, direction = Sort.Direction.ASC, size = 3) Pageable pageable) {

        Page<House> housePage;
        if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)) {
            housePage = houseService.getAll(pageable);
        } else {
            housePage = houseService.getHouseByUser(userService.getUserByPrincipal(principal), pageable);
        }
        Page<HouseGetDTO> houseGetDTOPage = housePage.map(objectEntity -> modelMapper.map(objectEntity, HouseGetDTO.class));
        return ResponseEntity.ok(houseGetDTOPage);
    }



    // create new house
    @PostMapping
    public ResponseEntity<?> createHouse(
              @RequestBody @Valid HousePostDTO housePostDTO
            , BindingResult bindingResultHouse
            , Principal principal
    ) {
        if (bindingResultHouse.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResultHouse));
        }
        House house = modelMapper.map(housePostDTO, House.class);
        try {
            houseService.saveToBase(principal, house);

        } catch (IOException e) {
            System.out.println("Something wrong");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




    // request form edit house by id
    @GetMapping("/{id}")
    public ResponseEntity<?> houseEdit(
            @PathVariable Long id
            , Principal principal) {

        if (principal != null && houseService.getById(id).isPresent()) {
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)
                    || houseService.getHouseById(id).orElseThrow().getUser().equals(userService.getUserByPrincipal(principal))
            ) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseHouseGetDTO(id));
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // update house
    @PutMapping("/{id}")
    public ResponseEntity<?> houseUpdate(
            @RequestBody @Valid HousePostDTO housePostDTO
            , BindingResult bindingResultHouse
            , @PathVariable long id
            , Principal principal
    ) {
        if (bindingResultHouse.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResultHouse));
        }

        if (principal != null && houseService.getById(id).isPresent()) {
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)
                    || houseService.getHouseById(id).orElseThrow().getUser().equals(userService.getUserByPrincipal(principal))
            ) {
                try {
                    House house = modelMapper.map(housePostDTO, House.class);
                    houseService.update(house, id);
                    return new ResponseEntity<>(HttpStatus.OK);
                } catch (IOException e) {
                    System.out.println("Something wrong");
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> houseDelete(@PathVariable("id") Long id, Principal principal) {
        if (principal != null && houseService.getById(id).isPresent()) {
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)
                    || houseService.getHouseById(id).orElseThrow().getUser().equals(userService.getUserByPrincipal(principal))
            ) {
                houseService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }








    @PostMapping("/{houseId}/prebooking")
    public ResponseEntity<?> housePreBooking(
            @PathVariable("houseId") long houseId
            ,@RequestBody @Valid OrderHistoryPostDTO orderHistoryPostDTO
            , BindingResult bindingResult
            ) {

        System.out.println(orderHistoryPostDTO);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }


        OrderHistory orderHistory = modelMapper.map(orderHistoryPostDTO, OrderHistory.class);

        orderHistory.setDataBookingEnd(orderHistory.getDataBookingStart().plusDays(orderHistory.getNumDaysBooking()));
        House house = houseService.getHouseById(houseId).orElseThrow();
        if (houseService.isDateFree(orderHistory, houseId) && house.getNumTourists() >= orderHistory.getNumTourists()) {
            orderHistory.setHouse(house);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(orderHistory);
        }


        return new ResponseEntity<>(HttpStatus.OK);


    }



    @PostMapping("/{houseId}/booking")
    public String hoseBooking(
            @PathVariable("houseId") long houseId,
            OrderHistory orderHistory,
            Model model,
            Principal principal) {

        if (houseService.getHouseById(houseId).isPresent() && principal != null) {
            House house = houseService.getHouseById(houseId).get();
            orderHistory.setHouse(house);
            orderHistoryService.saveToBase(orderHistory, houseId, principal);
            model.addAttribute("message", "Заброньовано");
        } else {
            model.addAttribute("message", "Щось пішло не так. Спробуйте знову.");
        }

        return "/message";

    }


    private Map<String, String> mapErrors (BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", FieldError::getDefaultMessage));
    }






    private HouseGetDTO responseHouseGetDTO(long id) {
        House house = houseService.getById(id).orElseThrow();
        return modelMapper.map(house, HouseGetDTO.class);
    }

}