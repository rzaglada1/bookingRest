package com.rzaglada1.bookingRest.controllers.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Violation {
    private String errorMessage;
}
