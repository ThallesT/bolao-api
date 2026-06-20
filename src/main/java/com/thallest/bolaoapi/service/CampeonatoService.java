package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.Campeonato;
import com.thallest.bolaoapi.repository.CampeonatoRepository;
import com.thallest.bolaoapi.web.dto.CampeonatoRequest;
import com.thallest.bolaoapi.web.dto.CampeonatoResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;

    public CampeonatoService(CampeonatoRepository campeonatoRepository) {
        this.campeonatoRepository = campeonatoRepository;
    }

    public CampeonatoResponse create(CampeonatoRequest request) {
        validateDates(request);

        Campeonato campeonato = new Campeonato();
        apply(campeonato, request);

        return toResponse(campeonatoRepository.save(campeonato));
    }

    @Transactional(readOnly = true)
    public List<CampeonatoResponse> findAll() {
        return campeonatoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CampeonatoResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public CampeonatoResponse update(Long id, CampeonatoRequest request) {
        validateDates(request);

        Campeonato campeonato = getEntity(id);
        apply(campeonato, request);

        return toResponse(campeonatoRepository.save(campeonato));
    }

    public void delete(Long id) {
        campeonatoRepository.delete(getEntity(id));
    }

    public Campeonato getEntity(Long id) {
        return campeonatoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Championship not found: " + id));
    }

    private void validateDates(CampeonatoRequest request) {
        if (request.startDate() != null && request.endDate() != null
            && request.endDate().isBefore(request.startDate())) {
            throw new BusinessException("Championship endDate cannot be before startDate.");
        }
    }

    private void apply(Campeonato campeonato, CampeonatoRequest request) {
        campeonato.setName(request.name().trim());
        campeonato.setSeason(request.season().trim());
        campeonato.setDescription(request.description());
        campeonato.setStartDate(request.startDate());
        campeonato.setEndDate(request.endDate());
        campeonato.setActive(request.active());
    }

    private CampeonatoResponse toResponse(Campeonato campeonato) {
        return new CampeonatoResponse(
            campeonato.getId(),
            campeonato.getName(),
            campeonato.getSeason(),
            campeonato.getDescription(),
            campeonato.getStartDate(),
            campeonato.getEndDate(),
            campeonato.isActive(),
            campeonato.getCreatedAt()
        );
    }
}

