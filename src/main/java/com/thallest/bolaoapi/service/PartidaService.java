package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.Campeonato;
import com.thallest.bolaoapi.domain.Partida;
import com.thallest.bolaoapi.repository.PartidaRepository;
import com.thallest.bolaoapi.web.dto.PartidaRequest;
import com.thallest.bolaoapi.web.dto.PartidaResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final CampeonatoService campeonatoService;

    public PartidaService(PartidaRepository partidaRepository, CampeonatoService campeonatoService) {
        this.partidaRepository = partidaRepository;
        this.campeonatoService = campeonatoService;
    }

    public PartidaResponse create(PartidaRequest request) {
        validateScores(request.homeScore(), request.awayScore());

        Partida partida = new Partida();
        apply(partida, request);

        return toResponse(partidaRepository.save(partida));
    }

    @Transactional(readOnly = true)
    public List<PartidaResponse> findAll() {
        return partidaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PartidaResponse findById(UUID id) {
        return toResponse(getEntity(id));
    }

    public PartidaResponse update(UUID id, PartidaRequest request) {
        validateScores(request.homeScore(), request.awayScore());

        Partida partida = getEntity(id);
        apply(partida, request);

        return toResponse(partidaRepository.save(partida));
    }

    public void delete(UUID id) {
        partidaRepository.delete(getEntity(id));
    }

    public Partida getEntity(UUID id) {
        return partidaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
    }

    private void validateScores(Integer homeScore, Integer awayScore) {
        if ((homeScore == null) != (awayScore == null)) {
            throw new BusinessException("Home and away scores must be informed together.");
        }
        if (homeScore != null && (homeScore < 0 || awayScore < 0)) {
            throw new BusinessException("Scores cannot be negative.");
        }
    }

    private void apply(Partida partida, PartidaRequest request) {
        Campeonato campeonato = campeonatoService.getEntity(request.championshipId());

        partida.setChampionship(campeonato);
        partida.setHomeTeam(request.homeTeam().trim());
        partida.setAwayTeam(request.awayTeam().trim());
        partida.setMatchDate(request.matchDate());
        partida.setRoundName(request.roundName());
        partida.setStage(request.stage());
        partida.setStatus(request.status());
        partida.setHomeScore(request.homeScore());
        partida.setAwayScore(request.awayScore());
    }

    private PartidaResponse toResponse(Partida partida) {
        return new PartidaResponse(
            partida.getId(),
            partida.getChampionship().getId(),
            partida.getHomeTeam(),
            partida.getAwayTeam(),
            partida.getMatchDate(),
            partida.getRoundName(),
            partida.getStage(),
            partida.getStatus(),
            partida.getHomeScore(),
            partida.getAwayScore(),
            partida.getCreatedAt()
        );
    }
}

