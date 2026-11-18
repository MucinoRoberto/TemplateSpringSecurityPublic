package dev.mucino.backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.mucino.backend.entities.User;
import dev.mucino.backend.repositories.UserRepository;
import dev.mucino.backend.security.SecurityUser;

@Service 
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository repository;
    
    public JpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " no encontrado en el sistema"));
        
       return new SecurityUser(user);
    }
}
    


