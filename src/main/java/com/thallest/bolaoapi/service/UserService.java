package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.repository.UserRepository;
import com.thallest.bolaoapi.web.dto.UserRequest;
import com.thallest.bolaoapi.web.dto.UserResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(UserRequest request) {
        validateUniqueEmail(request.email(), null);
        validateUniqueGoogleSubject(request.googleSubject(), null);

        UserEntity user = new UserEntity();
        apply(user, request);

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return toResponse(getEntity(id));
    }

    public UserResponse update(UUID id, UserRequest request) {
        validateUniqueEmail(request.email(), id);
        validateUniqueGoogleSubject(request.googleSubject(), id);

        UserEntity user = getEntity(id);
        apply(user, request);

        return toResponse(userRepository.save(user));
    }

    public void delete(UUID id) {
        userRepository.delete(getEntity(id));
    }

    public UserEntity getEntity(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private void validateUniqueEmail(String email, UUID currentId) {
        boolean exists = currentId == null
            ? userRepository.existsByEmailIgnoreCase(email)
            : userRepository.existsByEmailIgnoreCaseAndIdNot(email, currentId);

        if (exists) {
            throw new BusinessException("Email already registered: " + email);
        }
    }

    private void validateUniqueGoogleSubject(String googleSubject, UUID currentId) {
        if (googleSubject == null || googleSubject.isBlank()) {
            return;
        }

        boolean exists = currentId == null
            ? userRepository.existsByGoogleSubject(googleSubject.trim())
            : userRepository.existsByGoogleSubjectAndIdNot(googleSubject.trim(), currentId);

        if (exists) {
            throw new BusinessException("Google subject already linked to another user.");
        }
    }

    private void apply(UserEntity user, UserRequest request) {
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhotoUrl(request.photoUrl());
        user.setGoogleSubject(request.googleSubject() == null || request.googleSubject().isBlank()
            ? null
            : request.googleSubject().trim());
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhotoUrl(),
            user.getGoogleSubject(),
            user.getCreatedAt()
        );
    }
}

