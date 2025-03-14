package com.example.Boutique_Final.service;

import com.example.Boutique_Final.model.User;
import com.example.Boutique_Final.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(user -> {
                    logger.info("User found: {}", email);
                    return new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            user.getAuthorities()
                    );
                })
                .orElseThrow(() -> {
                    logger.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found: " + email);
                });
    }
}
