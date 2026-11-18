package dev.mucino.backend.dtos;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public class UserUpdateDto {

    @Size(min = 4, max = 30, message = "El username debe tener entre 4 y 30 caracteres")
    private String username;

    private String password; 

    private Boolean enabled; 

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<UUID> getRoles() {
        return roles;
    }

    public void setRoles(List<UUID> roles) {
        this.roles = roles;
    }

    
}
