package com.rzaglada1.bookingRest.repositories;

import com.rzaglada1.bookingRest.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable("user")
    Optional<User> findByEmail (String email);

    @Cacheable("user")
    Optional<User> findById (long id);

    @Query("SELECT COUNT(u) FROM User u")
    long size ();

}
