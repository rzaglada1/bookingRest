package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.UserGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.UserPostUpdateDTO;
import com.rzaglada1.bookingRest.dto.dto_post.simpleDTO.UserPostNewDTO;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.services.UserService;
import com.rzaglada1.bookingRest.token.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
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


    @Operation(summary = "Get users list (only for role 'ADMIN')")
    @ApiResponses(value =
            {
            @ApiResponse(responseCode = "200", description = "Found the all users", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            } )

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserGetDTO>> getAllUsersPage(
            @Parameter(description = "pageable properties", required = true, name = "Pageable", in = ParameterIn.QUERY)
            @ParameterObject @PageableDefault(size = 3) Pageable pageable) {

        Page<User> pageUser = userService.getAllPageable(pageable);
        Page<UserGetDTO> userGetDTOPage = pageUser.map(objectEntity -> modelMapper.map(objectEntity, UserGetDTO.class));
        return ResponseEntity.ok(userGetDTOPage);
    }


    @Operation(summary = "Get user by id")
    @ApiResponses(value =
            {
            @ApiResponse(responseCode = "200", description = "Found the user by id", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Users not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping({"/{id}"})
    public ResponseEntity<UserGetDTO> getUsersById(
            @Parameter(description = "user id", required = true, name = "id", in = ParameterIn.QUERY)
            @PathVariable long id, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        long idPrincipal = user.getId();

        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(responseUserGetDTO(id));
        } else if (idPrincipal == id) {
            return ResponseEntity.ok(responseUserGetDTO(id));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //new user
    @Operation(summary = "Create new user")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "User created", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
            })

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserPostNewDTO userPostNewDTO
            , BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }
        if (userService.findByEmail(userPostNewDTO.getEmail()).isPresent()) {
            Map<String, String> errorIsUserPresent = Map.of("emailError", "such user already exists");
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorIsUserPresent);
        }
        User user = modelMapper.map(userPostNewDTO, User.class);
        if (userService.saveToBase(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "User update")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "User updated", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @PutMapping({"/{id}"})
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserPostUpdateDTO userPostUpdateDTO
            , BindingResult bindingResult
            , @PathVariable long id
            , Principal principal
    ) {
        User user = userService.getUserByPrincipal(principal);
        long userId = user.getId();

        if (userId == id) {
            Set<Role> roleSet = new HashSet<>();
            if (user.getRoles().contains(Role.ROLE_ADMIN)) {
                roleSet.add(Role.ROLE_ADMIN);
            } else {
                roleSet.add(Role.ROLE_USER);
            }
            userPostUpdateDTO.setRoles(roleSet);

            return getResponseEntity(userPostUpdateDTO, bindingResult, id);

        } else if (user.getRoles().contains(Role.ROLE_ADMIN)) {

            return getResponseEntity(userPostUpdateDTO, bindingResult, id);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "User delete")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "User deleted", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> userDelete(Principal principal, @PathVariable long id) {
        User user = userService.getUserByPrincipal(principal);
        long userId = user.getId();
        try {
            if (userId == id || user.getRoles().contains(Role.ROLE_ADMIN)) {
                jwtService.revokeAllUserTokens(userService.getById(id).orElseThrow());
                userService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "User request param")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "user properties got", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid validation", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping("/param")
    public ResponseEntity<UserGetDTO> loginParam(Principal principal) {
        return ResponseEntity.ok(responseUserGetDTO(principal));
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


    private Map<String, String> mapErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f -> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }


    private UserGetDTO responseUserGetDTO(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        return modelMapper.map(user, UserGetDTO.class);
    }

    private UserGetDTO responseUserGetDTO(long id) {
        User user = userService.getById(id).orElseThrow();
        return modelMapper.map(user, UserGetDTO.class);
    }

}
