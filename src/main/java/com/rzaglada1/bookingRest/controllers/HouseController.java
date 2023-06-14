package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.HouseGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.HousePostDTO;
import com.rzaglada1.bookingRest.models.House;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.services.HouseService;
import com.rzaglada1.bookingRest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final UserService userService;


    private final ModelMapper modelMapper = new ModelMapper();


    // request form all  house
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get houses list")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Found user houses", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            } )
    @GetMapping
    public ResponseEntity<Page<HouseGetDTO>> houseAll(
            Principal principal
            , @ParameterObject @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 3) Pageable pageable) {

        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();

        if (principal != null) {
            Page<House> housePage;
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)) {
                housePage = houseService.getAll(pageable);
            } else {
                housePage = houseService.getHouseByUser(userService.getUserByPrincipal(principal), pageable);
            }
            Page<HouseGetDTO> houseGetDTOPage = housePage.map(objectEntity -> modelMapper.map(objectEntity, HouseGetDTO.class));
            return ResponseEntity.ok().cacheControl(cacheControl).body(houseGetDTOPage);
        }
        return ResponseEntity.badRequest().body(null);
    }


    // create new house
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create new house")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "House created", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })
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
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get house by id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Found the house by id", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Users not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<HouseGetDTO> houseEdit(
            @PathVariable Long id
            ) {

        if (houseService.getById(id).isPresent()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseHouseGetDTO(id));
            }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // update house
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "House update")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "House updated", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

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
        if (houseService.getById(id).isPresent()) {
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)
                    || houseService.getById(id).orElseThrow().getUser().getId() == userService.getUserByPrincipal(principal).getId()
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


    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "House delete")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "House deleted", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> houseDelete(@PathVariable("id") Long id, Principal principal) {
        if (principal != null && houseService.getById(id).isPresent()) {
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)
                    || houseService.getById(id).orElseThrow().getUser().getId() == userService.getUserByPrincipal(principal).getId()
            ) {
                houseService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    private Map<String, String> mapErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f -> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }


    private HouseGetDTO responseHouseGetDTO(long id) {
        House house = houseService.getById(id).orElseThrow();
        return modelMapper.map(house, HouseGetDTO.class);
    }

}
