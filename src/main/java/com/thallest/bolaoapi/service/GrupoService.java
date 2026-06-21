package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.Campeonato;
import com.thallest.bolaoapi.domain.Grupo;
import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.repository.GrupoRepository;
import com.thallest.bolaoapi.web.dto.GrupoRequest;
import com.thallest.bolaoapi.web.dto.GrupoResponse;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.ResourceNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UserService userService;
    private final CampeonatoService campeonatoService;

    public GrupoService(
        GrupoRepository grupoRepository,
        UserService userService,
        CampeonatoService campeonatoService
    ) {
        this.grupoRepository = grupoRepository;
        this.userService = userService;
        this.campeonatoService = campeonatoService;
    }

    public GrupoResponse create(GrupoRequest request) {
        validateUniqueAccessCode(request.accessCode(), null);

        Grupo grupo = new Grupo();
        apply(grupo, request);

        return toResponse(grupoRepository.save(grupo));
    }

    @Transactional(readOnly = true)
    public List<GrupoResponse> findAll() {
        return grupoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GrupoResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public GrupoResponse update(Long id, GrupoRequest request) {
        validateUniqueAccessCode(request.accessCode(), id);

        Grupo grupo = getEntity(id);
        apply(grupo, request);

        return toResponse(grupoRepository.save(grupo));
    }

    public void delete(Long id) {
        grupoRepository.delete(getEntity(id));
    }

    public Grupo getEntity(Long id) {
        return grupoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));
    }

    private void validateUniqueAccessCode(String accessCode, Long currentId) {
        boolean exists = currentId == null
            ? grupoRepository.existsByAccessCodeIgnoreCase(accessCode)
            : grupoRepository.existsByAccessCodeIgnoreCaseAndIdNot(accessCode, currentId);

        if (exists) {
            throw new BusinessException("Access code already in use: " + accessCode);
        }
    }

    private void apply(Grupo grupo, GrupoRequest request) {
        UserEntity owner = userService.getEntity(request.ownerId());
        Campeonato campeonato = campeonatoService.getEntity(request.championshipId());
        Set<UserEntity> members = new LinkedHashSet<>();

        for (Long memberId : request.memberIds()) {
            members.add(userService.getEntity(memberId));
        }

        members.add(owner);

        grupo.setName(request.name().trim());
        grupo.setAccessCode(request.accessCode().trim().toUpperCase());
        grupo.setOwner(owner);
        grupo.setChampionship(campeonato);
        grupo.getMembers().clear();
        grupo.getMembers().addAll(members);
    }

    private GrupoResponse toResponse(Grupo grupo) {
        return new GrupoResponse(
            grupo.getId(),
            grupo.getName(),
            grupo.getAccessCode(),
            grupo.getOwner().getId(),
            grupo.getChampionship().getId(),
            grupo.getMembers().stream().map(UserEntity::getId).sorted().toList(),
            grupo.getCreatedAt()
        );
    }
}

