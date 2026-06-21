package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Partida;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, UUID> {
}

