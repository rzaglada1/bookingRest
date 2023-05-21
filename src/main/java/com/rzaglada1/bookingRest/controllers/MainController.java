package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.HousesFilter;
import com.rzaglada1.bookingRest.services.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final HouseService houseService;
    private final ModelMapper modelMapper = new ModelMapper();



    @PostMapping("/find")
    public ResponseEntity<?> houseFind(
              @RequestBody @Valid HousesFilter housesFilter
            , BindingResult bindingResult
            , @PageableDefault(size = 3) Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        String country;
        String city;
        LocalDate date;
        int days;
        int people;

        if (housesFilter.getCountry() == null || housesFilter.getCountry().length() == 0) {
            country = "%";
        } else {
            country = housesFilter.getCountry();
        }

        if (housesFilter.getCity() == null || housesFilter.getCity().length() == 0) {
            city = "%";
        } else {
            city = housesFilter.getCity();
        }

        if (housesFilter.getDate() == null) {
            date = LocalDate.parse("1970-01-01");
        } else {
            date = housesFilter.getDate();
        }

        if (housesFilter.getDays() == null) {
            days = 1;
        } else {
            days = housesFilter.getDays();
        }

        if (housesFilter.getPeople() == null) {
            people = 1;
        } else {
            people = housesFilter.getPeople();
        }

        Page<House> housePage = houseService.filterHouses(country, city, date, days, people, pageable);
        Page<HouseGetDTO> houseGetDTOPage = housePage.map(objectEntity -> modelMapper.map(objectEntity, HouseGetDTO.class));
        return ResponseEntity.ok(houseGetDTOPage);
    }




    private Map<String, String> mapErrors (BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", FieldError::getDefaultMessage));
    }

}
