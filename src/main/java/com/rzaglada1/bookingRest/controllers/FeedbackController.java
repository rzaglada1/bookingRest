package com.rzaglada1.bookingRest.controllers;

import com.rzaglada1.bookingRest.dto.dto_get.FeedbackGetDTO;
import com.rzaglada1.bookingRest.dto.dto_post.FeedbackPostDTO;
import com.rzaglada1.bookingRest.models.Feedback;
import com.rzaglada1.bookingRest.services.FeedbackService;
import com.rzaglada1.bookingRest.services.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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


    @GetMapping("/{houseId}")
    public ResponseEntity<List<FeedbackGetDTO>> getFeedbackByHouseId(
            @PathVariable Long houseId
    ) {
        ModelMapper modelMapper = new ModelMapper();
        List<Feedback> feedbackList = feedbackService.getFeedbackByHouseId(houseId);

        Type listType = new TypeToken<List<FeedbackGetDTO>>() {
        }.getType();
        List<FeedbackGetDTO> feedbackGetDTOList = modelMapper.map(feedbackList, listType);
        return ResponseEntity.ok(feedbackGetDTOList);
    }


    @PostMapping("/{houseId}")
    public ResponseEntity<?> houseFeedback(
            @PathVariable("houseId") Long houseId
            , @RequestBody @Valid FeedbackPostDTO feedbackPostDTO
            , BindingResult bindingResult
            , Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        if (houseService.getById(houseId).isPresent()) {
            Feedback feedback = modelMapper.map(feedbackPostDTO, Feedback.class);
            feedbackService.saveToBase(feedback, houseId, principal);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    private Map<String, String> mapErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", FieldError::getDefaultMessage));
    }


}
