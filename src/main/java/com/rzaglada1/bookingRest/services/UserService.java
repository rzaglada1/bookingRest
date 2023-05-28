package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.dto.dto_post.UserPostUpdateDTO;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.models.enams.Role;
import com.rzaglada1.bookingRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @CacheEvict(value="user", allEntries=true)
    public boolean saveToBase(User user) {
        boolean isAllOk = false;
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setActive(true);
            // if first user then role = ADMIN
            if (userRepository.findAll().isEmpty()) {
                user.getRoles().clear();
                user.getRoles().add(Role.ROLE_ADMIN);
            } else {
                user.getRoles().clear();
                user.getRoles().add(Role.ROLE_USER);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            isAllOk = true;
        }
        return isAllOk;
    }

    @CacheEvict(value="user", allEntries=true)
    public void deleteById(long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow());
    }


    public Page<User> getAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }



    public Optional<User> getById(long id) {
        return userRepository.findById(id);
    }





    @CacheEvict(value="user", allEntries=true)
    public boolean update(UserPostUpdateDTO user, long userId) {
        boolean isAllOk = false;
        if (userRepository.findById(userId).isPresent()) {
            User userUpdate = userRepository.findById(userId).get();
            //update Role
            userUpdate.setRoles(user.getRoles());
            userUpdate.setFirstName(user.getFirstName());
            userUpdate.setLastName(user.getLastName());
            userUpdate.setPhone(user.getPhone());

            if (user.getActive() == null) {
                userUpdate.setActive(true);
            } else {
                userUpdate.setActive(user.getActive());
            }

            if (user.getPasswordOld() !=null && user.getPasswordOld().length() != 0 ) {
                userUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(userUpdate);
            isAllOk = true;
        }
        return isAllOk;
    }



    public Optional<User> findByEmail (String email) {
        return userRepository.findByEmail(email);
    }




    public User getUserByPrincipal(Principal principal) {
        System.out.println("principal1");
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        System.out.println("principal2");
        return user;
    }

    public Boolean isTruePassword(Long id, String password) {
        boolean isCheck = false;
        if (userRepository.findById(id).isPresent()) {
            isCheck = passwordEncoder.matches(password, userRepository.findById(id).get().getPassword());
        }
        return isCheck;
    }


}
