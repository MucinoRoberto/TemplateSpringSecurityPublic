package dev.mucino.backend.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import dev.mucino.backend.entities.User;
import dev.mucino.backend.repositories.UserRepository;

@Component 
public class CustomPermissionEvaluator implements PermissionEvaluator{

    private final UserRepository userRepository;

    public CustomPermissionEvaluator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, @Nullable Object targetDomainObject, Object permission) {

        if (authentication == null || permission == null) {
            return false;
        }
        String requiredPermission = permission.toString();

        try {
            UUID userId = UUID.fromString(authentication.getName());
            
            User user = userRepository.findById(userId).orElse(null);
            if (user == null || user.getRoles() == null) {
                return false;
            }

            String[] parts = requiredPermission.split(":");
            String module = parts[0];
            String action = parts[1];

            return user.getRoles().stream().anyMatch(role -> {
                Map<String, Object> jsonPermissions = role.getPermissions();
                if (jsonPermissions == null || !jsonPermissions.containsKey(module)) {
                    return false;
                }

                @SuppressWarnings("unchecked")
                List<String> actions = (List<String>) jsonPermissions.get(module);
                return actions != null && actions.contains(action);
            });

        } catch (IllegalArgumentException e) {
            return false; 
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
       return hasPermission(authentication, null, permission);
    }

}
