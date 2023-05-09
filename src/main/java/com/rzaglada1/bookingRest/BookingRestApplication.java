package com.rzaglada1.bookingRest;

import com.rzaglada1.bookingRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BookingRestApplication {

	private static UserRepository userRepository;

	@Autowired
	public BookingRestApplication (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public static void main(String[] args) {
		SpringApplication.run(BookingRestApplication.class, args);


	}

}
