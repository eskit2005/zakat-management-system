package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.RegisterRequest;
import com.example.zakat.management.system.dtos.response.UserResponse;
import com.example.zakat.management.system.entities.User;
import com.example.zakat.management.system.events.UserRegisteredEvent;
import com.example.zakat.management.system.exceptions.DuplicateResourceException;
import com.example.zakat.management.system.mappers.UserMapper;
import com.example.zakat.management.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
//
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.countByEmail(request.getEmail()) > 0) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User saved = userRepository.save(user);

        // ---------------------------------------------------------------
        // EMAIL HOOK — send welcome email to new user here

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(user.getName(), user.getEmail(), user.getRole()));

        return userMapper.toResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
