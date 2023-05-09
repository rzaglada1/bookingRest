package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail (String email);

    @Query("SELECT COUNT(u) FROM User u")
    long size ();

}
