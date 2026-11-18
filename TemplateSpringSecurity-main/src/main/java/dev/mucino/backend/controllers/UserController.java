package dev.mucino.backend.controllers;

// import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.mucino.backend.dtos.UserDto;
import dev.mucino.backend.dtos.UserUpdateDto;
import dev.mucino.backend.entities.User;
import dev.mucino.backend.exceptions.UserNotFoundException;
import dev.mucino.backend.services.UserService;
import jakarta.validation.Valid;

@CrossOrigin (origins = "https://mucinoapp.com")
@RestController 
@RequestMapping ("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping ("/{uuid}")
    @PreAuthorize("hasPermission(null, 'users:read')")
    public User findById(@PathVariable UUID uuid){
        User user = userService.findById(uuid).orElseThrow(()-> new UserNotFoundException("Error: User doesnt exist")); 
        return user;
        
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'users:read')")
    public List<User> list(){
        return userService.findAll();
    }
    

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, BindingResult result) {
        
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'users:create')")
    public ResponseEntity<?> createUserByAdmin(@Valid @RequestBody UserDto dto,  BindingResult result) {
        
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        User newUser = userService.createUserWithSpecificRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'users:update')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto dto, BindingResult result) {
        
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        User updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, Object> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
