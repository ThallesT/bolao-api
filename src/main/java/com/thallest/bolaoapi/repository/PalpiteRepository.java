package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Palpite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalpiteRepository extends JpaRepository<Palpite, Long> {

    boolean existsByGroupIdAndMatchIdAndUserId(Long groupId, Long matchId, Long userId);

    boolean existsByGroupIdAndMatchIdAndUserIdAndIdNot(Long groupId, Long matchId, Long userId, Long id);

    List<Palpite> findByGroupId(Long groupId);
}

