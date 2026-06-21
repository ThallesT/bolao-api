package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.service.GrupoService;
import com.thallest.bolaoapi.web.dto.GrupoRequest;
import com.thallest.bolaoapi.web.dto.GrupoResponse;
import com.thallest.bolaoapi.web.dto.RankingResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GrupoController {

    private final GrupoService grupoService;
    private final com.thallest.bolaoapi.service.RankingService rankingService;

    public GrupoController(
        GrupoService grupoService,
        com.thallest.bolaoapi.service.RankingService rankingService
    ) {
        this.grupoService = grupoService;
        this.rankingService = rankingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoResponse create(@Valid @RequestBody GrupoRequest request) {
        return grupoService.create(request);
    }

    @GetMapping
    public List<GrupoResponse> findAll() {
        return grupoService.findAll();
    }

    @GetMapping("/{id}")
    public GrupoResponse findById(@PathVariable Long id) {
        return grupoService.findById(id);
    }

    @GetMapping("/{id}/ranking")
    public RankingResponse getRanking(@PathVariable Long id) {
        return rankingService.getGroupRanking(id);
    }

    @PutMapping("/{id}")
    public GrupoResponse update(@PathVariable Long id, @Valid @RequestBody GrupoRequest request) {
        return grupoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        grupoService.delete(id);
    }
}

