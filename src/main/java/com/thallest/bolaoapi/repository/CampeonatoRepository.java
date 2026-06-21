package com.thallest.bolaoapi.repository;

import com.thallest.bolaoapi.domain.Campeonato;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampeonatoRepository extends JpaRepository<Campeonato, UUID> {
}

