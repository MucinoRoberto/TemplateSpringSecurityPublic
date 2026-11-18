package dev.mucino.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.mucino.backend.dtos.UserDto;
import dev.mucino.backend.dtos.UserUpdateDto;
import dev.mucino.backend.entities.User;

public interface UserService {
    
    List<User> findAll();

    User saveUser (User user);

    boolean existByUsername(String username);

    Optional<User> findById(UUID uuid);

    User createUserWithSpecificRole(UserDto user);

    User updateUser(UUID id, UserUpdateDto userUpdateDto);

}
