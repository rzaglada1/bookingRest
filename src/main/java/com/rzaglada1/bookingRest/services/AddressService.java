package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.Address;
import com.rzaglada1.bookingRest.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;


    public void saveToBase (Address address) {
        repository.save(address);
    }

    public Optional<Address> getById (long id) {
        return repository.findById(id);
    }
}
