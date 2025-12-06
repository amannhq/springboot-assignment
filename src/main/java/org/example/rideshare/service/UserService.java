package org.example.rideshare.service;

import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return getUserByUsername(username);
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserRole() {
        return getCurrentUser().getRole();
    }
}
