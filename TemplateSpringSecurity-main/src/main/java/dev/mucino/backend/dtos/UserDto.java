package dev.mucino.backend.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 30, message = "El username debe tener entre 4 y 30 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private List<UUID> roles; 

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UUID> getRoles() {
        return roles;
    }

    public void setRoles(List<UUID> role) {
        this.roles = role;
    }

    
}