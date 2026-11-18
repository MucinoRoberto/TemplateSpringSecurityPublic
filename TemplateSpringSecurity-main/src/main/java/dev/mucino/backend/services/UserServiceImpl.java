package dev.mucino.backend.services;

// import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.mucino.backend.dtos.UserDto;
import dev.mucino.backend.dtos.UserUpdateDto;
import dev.mucino.backend.entities.Role;
import dev.mucino.backend.entities.User;
import dev.mucino.backend.exceptions.DataConflictException;
import dev.mucino.backend.exceptions.ResourceNotFoundException;
import dev.mucino.backend.exceptions.UserExistException;
import dev.mucino.backend.exceptions.UserNotFoundException;
import dev.mucino.backend.repositories.RoleRepository;
import dev.mucino.backend.repositories.UserRepository;


@Service 
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional (readOnly = true)
    public List<User> findAll() {
       return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<User> findById(UUID uuid) {
       return userRepository.findById(uuid);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            throw new UserExistException("El usuario ya existe");
        Role roleUser = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new ResourceNotFoundException("Error: Rol no encontrado en la BD"));

        user.setRoles(List.of(roleUser));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    @Override
    public boolean existByUsername(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'existByUsername'");
    }

    @Override
    @Transactional
    public User createUserWithSpecificRole(UserDto userDto) {
        
        if(userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserExistException("El usuario ya existe");
        }

        List<Role> rolesList;

        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            rolesList = (List<Role>) roleRepository.findAllById(userDto.getRoles());
            
            if (rolesList.isEmpty()) {
                throw new ResourceNotFoundException("Error: Ninguno de los roles proporcionados fue encontrado en la BD");
            }
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Error: Rol por defecto no encontrado"));
            rolesList = List.of(defaultRole);
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRoles(rolesList); 
        
        return userRepository.save(newUser);
    }

    @Override
    @Transactional 
    public User updateUser(UUID id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (dto.getUsername().length() < 4) 
                throw new DataConflictException("El username debe contener mas de 4 caracteres"); 
            if (dto.getUsername().length() > 30) 
                throw new DataConflictException("El username debe contener menos de 30 caracteres"); 
            if(userRepository.existsByUsername(dto.getUsername()))
                throw new UserExistException("El username ya existe");
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getEnabled() != null) {
            user.setEnabled(dto.getEnabled());
        }

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            List<Role> newRoles = (List<Role>) roleRepository.findAllById(dto.getRoles());
            if (newRoles.isEmpty()) {
                throw new ResourceNotFoundException("Error: Ninguno de los roles proporcionados fue encontrado");
            }
            user.setRoles(newRoles);
        }

        return userRepository.save(user);

    }

}
