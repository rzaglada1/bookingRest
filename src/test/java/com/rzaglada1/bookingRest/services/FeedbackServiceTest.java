package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.Feedback;
import com.rzaglada1.bookingRest.repositories.FeedbackRepository;
import com.rzaglada1.bookingRest.repositories.HouseRepository;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FeedbackServiceTest {

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    HouseRepository houseRepository;

    @Test
    void getFeedbackByHouseIdTest() {
        FeedbackService feedbackService = new FeedbackService(feedbackRepository, userRepository, houseRepository);
        Feedback feedback = Feedback.builder().rating(10).description("Test feedback").id(52).rating(9.0).build();
        List<Feedback> feedbackListExpect = List.of(feedback);

        when(feedbackRepository.getFeedbackByHouseId(1)).thenReturn(feedbackListExpect);
        List<Feedback> actualList = feedbackService.getFeedbackByHouseId(1);

        assertEquals(feedbackListExpect, actualList);
    }


}