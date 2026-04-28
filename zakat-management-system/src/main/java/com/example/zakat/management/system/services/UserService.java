package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.RegisterRequest;
import com.example.zakat.management.system.dtos.response.UserResponse;
import com.example.zakat.management.system.entities.Admin;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.entities.Donor;
import com.example.zakat.management.system.entities.User;
import com.example.zakat.management.system.events.UserRegisteredEvent;
import com.example.zakat.management.system.exceptions.DuplicateResourceException;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.UserMapper;
import com.example.zakat.management.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.countByEmail(request.getEmail()) > 0) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        User user;
        String role = request.getRole();

        if ("ADMIN".equals(role)) {
            user = new Admin();
        } else if ("DONOR".equals(role)) {
            user = new Donor();
        } else if ("BENEFICIARY".equals(role)) {
            user = new Beneficiary();
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User saved = userRepository.save(user);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(user.getName(), user.getEmail(), role));

        return userMapper.toResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
