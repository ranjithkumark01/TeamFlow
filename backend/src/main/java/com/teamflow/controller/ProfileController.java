package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.dto.request.ProfileUpdateRequest;
import com.teamflow.dto.response.UserResponse;
import com.teamflow.entity.User;
import com.teamflow.exception.BadRequestException;
import com.teamflow.exception.ResourceNotFoundException;
import com.teamflow.mapper.UserMapper;
import com.teamflow.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> currentUser(Authentication authentication) {
        User user = findCurrentUser(authentication);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", UserMapper.toResponse(user)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        User user = findCurrentUser(authentication);
        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new BadRequestException("Email is already registered.");
                });

        user.setName(request.name());
        user.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        return ResponseEntity.ok(ApiResponse.success("Profile updated", UserMapper.toResponse(userRepository.save(user))));
    }

    private User findCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", 0L));
    }
}

