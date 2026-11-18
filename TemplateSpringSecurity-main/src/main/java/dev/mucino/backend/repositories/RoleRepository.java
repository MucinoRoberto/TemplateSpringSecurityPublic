package dev.mucino.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.mucino.backend.entities.Role;


public interface RoleRepository extends CrudRepository<Role, UUID> {

    Optional<Role> findByName(String name);

}
