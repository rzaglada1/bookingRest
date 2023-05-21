package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.Feedback;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.repositories.FeedbackRepository;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository repository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    public void saveToBase(Feedback feedback, long idHouse, Principal principal) {
        if (houseRepository.findById(idHouse).isPresent()) {
            feedback.setHouse(houseRepository.findById(idHouse).get());
            feedback.setUser(getUserByPrincipal(principal));
            repository.save(feedback);
        }

    }

    public List<Feedback> getFeedbackByHouseId (long id) {
        return repository.getFeedbackByHouseId(id);
    }


    private User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow();
    }

}
