package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.Grupo;
import com.thallest.bolaoapi.domain.Palpite;
import com.thallest.bolaoapi.domain.Partida;
import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.repository.PalpiteRepository;
import com.thallest.bolaoapi.web.dto.PalpiteRequest;
import com.thallest.bolaoapi.web.dto.PalpiteResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PalpiteService {

    private final PalpiteRepository palpiteRepository;
    private final GrupoService grupoService;
    private final PartidaService partidaService;
    private final UserService userService;

    public PalpiteService(
        PalpiteRepository palpiteRepository,
        GrupoService grupoService,
        PartidaService partidaService,
        UserService userService
    ) {
        this.palpiteRepository = palpiteRepository;
        this.grupoService = grupoService;
        this.partidaService = partidaService;
        this.userService = userService;
    }

    public PalpiteResponse create(PalpiteRequest request) {
        throw new UnsupportedOperationException("Use create(UUID, PalpiteRequest)");
    }

    public PalpiteResponse create(UUID authenticatedUserId, PalpiteRequest request) {
        validateUniqueGuess(request.matchId(), authenticatedUserId, null);

        Palpite palpite = new Palpite();
        apply(palpite, authenticatedUserId, request);

        return toResponse(palpiteRepository.save(palpite));
    }

    @Transactional(readOnly = true)
    public List<PalpiteResponse> findAll() {
        return palpiteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PalpiteResponse findById(UUID id) {
        return toResponse(getEntity(id));
    }

    public PalpiteResponse update(UUID id, PalpiteRequest request) {
        throw new UnsupportedOperationException("Use update(UUID, UUID, PalpiteRequest)");
    }

    public PalpiteResponse update(UUID id, UUID authenticatedUserId, PalpiteRequest request) {
        ensureOwnership(id, authenticatedUserId);
        validateUniqueGuess(request.matchId(), authenticatedUserId, id);

        Palpite palpite = getEntity(id);
        apply(palpite, authenticatedUserId, request);

        return toResponse(palpiteRepository.save(palpite));
    }

    public void delete(UUID id) {
        throw new UnsupportedOperationException("Use delete(UUID, UUID)");
    }

    public void delete(UUID id, UUID authenticatedUserId) {
        ensureOwnership(id, authenticatedUserId);
        palpiteRepository.delete(getEntity(id));
    }

    private void validateUniqueGuess(UUID matchId, UUID userId, UUID currentId) {
        boolean exists = currentId == null
            ? palpiteRepository.existsByMatchIdAndUserId(matchId, userId)
            : palpiteRepository.existsByMatchIdAndUserIdAndIdNot(matchId, userId, currentId);

        if (exists) {
            throw new BusinessException("A user can only have one guess per match.");
        }
    }

    private Palpite getEntity(UUID id) {
        return palpiteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guess not found: " + id));
    }

    private void ensureOwnership(UUID guessId, UUID authenticatedUserId) {
        Palpite palpite = getEntity(guessId);
        if (!palpite.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException("Users can only manage their own guesses.");
        }
    }

    private void apply(Palpite palpite, UUID authenticatedUserId, PalpiteRequest request) {
        Grupo grupo = grupoService.getEntity(request.groupId());
        Partida partida = partidaService.getEntity(request.matchId());
        UserEntity user = userService.getEntity(authenticatedUserId);

        if (!partida.getMatchDate().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Guesses are frozen after the match starts.");
        }
        if (!grupo.getChampionship().getId().equals(partida.getChampionship().getId())) {
            throw new BusinessException("Guess group and match must belong to the same championship.");
        }
        if (request.homeScoreGuess() < 0 || request.awayScoreGuess() < 0) {
            throw new BusinessException("Guess scores cannot be negative.");
        }
        boolean memberOfGroup = grupo.getMembers().stream().anyMatch(member -> member.getId().equals(user.getId()));
        if (!memberOfGroup) {
            throw new BusinessException("User must be a member of the group to create a guess.");
        }

        palpite.setGroup(grupo);
        palpite.setMatch(partida);
        palpite.setUser(user);
        palpite.setHomeScoreGuess(request.homeScoreGuess());
        palpite.setAwayScoreGuess(request.awayScoreGuess());
    }

    private PalpiteResponse toResponse(Palpite palpite) {
        return new PalpiteResponse(
            palpite.getId(),
            palpite.getGroup().getId(),
            palpite.getMatch().getId(),
            palpite.getUser().getId(),
            palpite.getHomeScoreGuess(),
            palpite.getAwayScoreGuess(),
            palpite.getCreatedAt(),
            palpite.getUpdatedAt()
        );
    }
}

