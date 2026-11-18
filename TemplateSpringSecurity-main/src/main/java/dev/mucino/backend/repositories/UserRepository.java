package dev.mucino.backend.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.mucino.backend.entities.User;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User,UUID>{

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    
} 