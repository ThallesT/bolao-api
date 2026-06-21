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
import java.util.List;
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
        validateUniqueGuess(request.groupId(), request.matchId(), request.userId(), null);

        Palpite palpite = new Palpite();
        apply(palpite, request);

        return toResponse(palpiteRepository.save(palpite));
    }

    @Transactional(readOnly = true)
    public List<PalpiteResponse> findAll() {
        return palpiteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PalpiteResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public PalpiteResponse update(Long id, PalpiteRequest request) {
        validateUniqueGuess(request.groupId(), request.matchId(), request.userId(), id);

        Palpite palpite = getEntity(id);
        apply(palpite, request);

        return toResponse(palpiteRepository.save(palpite));
    }

    public void delete(Long id) {
        palpiteRepository.delete(getEntity(id));
    }

    private void validateUniqueGuess(Long groupId, Long matchId, Long userId, Long currentId) {
        boolean exists = currentId == null
            ? palpiteRepository.existsByGroupIdAndMatchIdAndUserId(groupId, matchId, userId)
            : palpiteRepository.existsByGroupIdAndMatchIdAndUserIdAndIdNot(groupId, matchId, userId, currentId);

        if (exists) {
            throw new BusinessException("A guess for this user, group and match already exists.");
        }
    }

    private Palpite getEntity(Long id) {
        return palpiteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guess not found: " + id));
    }

    private void apply(Palpite palpite, PalpiteRequest request) {
        Grupo grupo = grupoService.getEntity(request.groupId());
        Partida partida = partidaService.getEntity(request.matchId());
        UserEntity user = userService.getEntity(request.userId());

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

