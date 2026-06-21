package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.repository.UserRepository;
import com.thallest.bolaoapi.web.dto.UserRequest;
import com.thallest.bolaoapi.web.dto.UserResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.util.List;
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

        UserEntity user = new UserEntity();
        apply(user, request);

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public UserResponse update(Long id, UserRequest request) {
        validateUniqueEmail(request.email(), id);

        UserEntity user = getEntity(id);
        apply(user, request);

        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.delete(getEntity(id));
    }

    public UserEntity getEntity(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private void validateUniqueEmail(String email, Long currentId) {
        boolean exists = currentId == null
            ? userRepository.existsByEmailIgnoreCase(email)
            : userRepository.existsByEmailIgnoreCaseAndIdNot(email, currentId);

        if (exists) {
            throw new BusinessException("Email already registered: " + email);
        }
    }

    private void apply(UserEntity user, UserRequest request) {
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}

