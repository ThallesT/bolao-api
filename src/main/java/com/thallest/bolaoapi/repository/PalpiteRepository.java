package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Palpite;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalpiteRepository extends JpaRepository<Palpite, UUID> {

    boolean existsByMatchIdAndUserId(UUID matchId, UUID userId);

    boolean existsByMatchIdAndUserIdAndIdNot(UUID matchId, UUID userId, UUID id);
}

