package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByAccessCodeIgnoreCase(String accessCode);

    boolean existsByAccessCodeIgnoreCaseAndIdNot(String accessCode, Long id);
}

