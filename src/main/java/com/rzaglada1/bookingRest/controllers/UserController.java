package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.UserGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostUpdateDTO;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.services.UserService;
import com.rzaglada1.bookingRest.token.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper = new ModelMapper();


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserGetDTO>> getAllUsersPage(@PageableDefault(size = 3) Pageable pageable) {
        Page<User> pageUser = userService.getAllPageable(pageable);
        Page<UserGetDTO> userGetDTOPage = pageUser.map(objectEntity -> modelMapper.map(objectEntity, UserGetDTO.class));
        return ResponseEntity.ok(userGetDTOPage);
    }


    @GetMapping({"/{id}"})
    public ResponseEntity<?> getUsersById(@PathVariable long id, Principal principal) {
        long idPrincipal = userService.getUserByPrincipal(principal).getId();

        if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(responseUserGetDTO(id));
        } else if (idPrincipal == id) {
            return ResponseEntity.ok(responseUserGetDTO(id));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //new user
    @PostMapping("/new")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserPostDTO userPostDTO
            , BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (userService.findByEmail(userPostDTO.getEmail()).isPresent()) {
            Map<String, String> errorPasswordOld = Map.of("emailError", "such user already exists");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorPasswordOld);
        }
        User user = modelMapper.map(userPostDTO, User.class);
        if (userService.saveToBase(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    //update for role admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/update/{id}"})
    public ResponseEntity<?> updateUserAdmin(
            @RequestBody @Valid UserPostUpdateDTO userPostUpdateDTO
            , BindingResult bindingResult
            , @PathVariable long id
    ) {

        return getResponseEntity(userPostUpdateDTO, bindingResult, id);
    }

    //update for role user
    @PutMapping({"/update"})
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserPostUpdateDTO userPostUpdateDTO
            , BindingResult bindingResult
            , Principal principal
    ) {
        long id = userService.getUserByPrincipal(principal).getId();
        Set<Role> roleSet = new HashSet<>();
        if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN)) {
            roleSet.add(Role.ROLE_ADMIN);
        } else {
            roleSet.add(Role.ROLE_USER);
        }
        userPostUpdateDTO.setRoles(roleSet);
        return getResponseEntity(userPostUpdateDTO, bindingResult, id);
    }


    private ResponseEntity<?> getResponseEntity(@RequestBody @Valid UserPostUpdateDTO userPostUpdateDTO, BindingResult bindingResult, long id) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (
                userPostUpdateDTO.getPassword() != null
                        && userPostUpdateDTO.getPassword().length() != 0
                        && (userPostUpdateDTO.getPasswordOld() == null || !userService.isTruePassword(id, userPostUpdateDTO.getPasswordOld()))
        ) {
            Map<String, String> errorPasswordOld = Map.of("passwordOldError", "the old password is not valid");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorPasswordOld);
        }

        if (
                userPostUpdateDTO.getPasswordOld() != null
                        && userPostUpdateDTO.getPassword() != null
                        && userPostUpdateDTO.getPasswordOld().length() != 0
                        && userPostUpdateDTO.getPassword().length() == 0
        ) {
            Map<String, String> errorPassword = Map.of("passwordError", "field password is empty");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorPassword);
        }
        if (userService.update(userPostUpdateDTO, id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(value = {"/delete", "/delete/{id}"})
    public ResponseEntity<?> userDelete(Principal principal, @PathVariable(required = false) String id) {
            User user = userService.getUserByPrincipal(principal);
            long userId = user.getId();
            if (userService.getUserByPrincipal(principal).getRoles().contains(Role.ROLE_ADMIN) && id != null) {
                userId = Long.parseLong(id);
            }
            try {
                userService.deleteById(userId);
                jwtService.revokeAllUserTokens(user);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/param")
    public ResponseEntity<?> loginParam(Principal principal) {
        long idPrincipal = userService.getUserByPrincipal(principal).getId();
        return ResponseEntity.ok(responseUserGetDTO(idPrincipal));
    }


    private Map<String, String> mapErrors (BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f-> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }


    private UserGetDTO responseUserGetDTO(long id) {
        User user = userService.getById(id).orElseThrow();
        return modelMapper.map(user, UserGetDTO.class);
    }


}
