package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.FeedbackGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.FeedbackPostDTO;
import com.rzaglada1.bookingRest.models.Feedback;
import com.rzaglada1.bookingRest.services.FeedbackService;
import com.rzaglada1.bookingRest.services.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final HouseService houseService;
    private final ModelMapper modelMapper = new ModelMapper();


    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get feedbacks by house id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "The id house is on the feedbacks list", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Feedbacks not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @GetMapping("/{idHouse}")
    public ResponseEntity<List<FeedbackGetDTO>> getFeedbackByHouseId(
            @PathVariable Long idHouse
    ) {
        ModelMapper modelMapper = new ModelMapper();
        List<Feedback> feedbackList = feedbackService.getFeedbackByHouseId(idHouse);

        Type listType = new TypeToken<List<FeedbackGetDTO>>() {
        }.getType();
        List<FeedbackGetDTO> feedbackGetDTOList = modelMapper.map(feedbackList, listType);
        return ResponseEntity.ok(feedbackGetDTOList);
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create feedback by house id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "Feedback created", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "403", description = "Invalid token or role not 'ADMIN'", content = @Content)
            })

    @PostMapping("/{idHouse}")
    public ResponseEntity<?> houseFeedback(
            @PathVariable("idHouse") Long idHouse
            , @RequestBody @Valid FeedbackPostDTO feedbackPostDTO
            , BindingResult bindingResult
            , Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (houseService.getById(idHouse).isPresent()) {
            Feedback feedback = modelMapper.map(feedbackPostDTO, Feedback.class);
            feedbackService.saveToBase(feedback, idHouse, principal);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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


}
