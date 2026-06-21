package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    boolean existsByGoogleSubject(String googleSubject);

    boolean existsByGoogleSubjectAndIdNot(String googleSubject, UUID id);

    Optional<UserEntity> findByGoogleSubject(String googleSubject);
}

