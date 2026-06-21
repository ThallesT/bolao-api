package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Grupo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, UUID> {

    boolean existsByAccessCodeIgnoreCase(String accessCode);

    boolean existsByAccessCodeIgnoreCaseAndIdNot(String accessCode, UUID id);
}

