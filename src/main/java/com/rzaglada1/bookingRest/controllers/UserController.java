package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.UserGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostDTO;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();


    @GetMapping
    public ResponseEntity<Page<UserGetDTO>> getAllUsersPage(@PageableDefault(size = 3, page = 0) Pageable pageable) {

        Page<User> pageUser = userService.getAllPageable(pageable);
        Page<UserGetDTO> userGetDTOList = pageUser.map(objectEntity -> modelMapper.map(objectEntity, UserGetDTO.class));
        return ResponseEntity.ok(userGetDTOList);
    }


    @GetMapping({"/{id}"})
    public ResponseEntity<UserGetDTO> getUsersById(@PathVariable long id) {
        User user = userService.getById(id).orElseThrow();
        UserGetDTO userGetDTO = modelMapper.map(user, UserGetDTO.class);

        return ResponseEntity.ok(userGetDTO);
    }

    //new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserPostDTO userPostDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (userService.findByEmail(userPostDTO.getEmail() ) != null) {
            Map<String,String> errorPasswordOld = Map.of("emailError", "such user already exists");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorPasswordOld);
        }

        System.out.println("UserPostDTO new: " + userPostDTO);

        User user = modelMapper.map(userPostDTO, User.class);
        if (userService.saveToBase(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PutMapping({"/{id}/update"})
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserPostDTO userPostDTO
            , BindingResult bindingResult
            , @PathVariable long id
    ) {

        System.out.println("userDTO " + userPostDTO);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (userPostDTO.getPasswordOld() != null && userPostDTO.getPasswordOld().length() != 0
                && !userService.isTruePassword(id, userPostDTO.getPasswordOld())) {
            Map<String,String> errorPasswordOld = Map.of("passwordOldError", "the old password is not valid");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorPasswordOld);

        }

        if (userService.update(userPostDTO, id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping (value = {"/delete", "/{id}/delete"})
    public ResponseEntity<?> userDelete(Principal principal, @PathVariable(required = false) String id) {
        if (userService.getUserByPrincipal(principal) != null) {
//            long userId = userService.getUserByPrincipal(principal).getId();
            long userId = Long.parseLong(id);

            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN) && id != null) {
                userId = Long.parseLong(id);
            }
            try {
                userService.deleteById(userId);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Map<String, String> mapErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", FieldError::getDefaultMessage));
    }





}
