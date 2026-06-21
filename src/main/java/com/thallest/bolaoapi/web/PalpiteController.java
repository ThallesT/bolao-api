package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.service.PalpiteService;
import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.web.dto.PalpiteRequest;
import com.thallest.bolaoapi.web.dto.PalpiteResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/guesses")
public class PalpiteController {

    private final PalpiteService palpiteService;

    public PalpiteController(PalpiteService palpiteService) {
        this.palpiteService = palpiteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PalpiteResponse create(
        @AuthenticationPrincipal UserEntity currentUser,
        @Valid @RequestBody PalpiteRequest request
    ) {
        return palpiteService.create(currentUser.getId(), request);
    }

    @GetMapping
    public List<PalpiteResponse> findAll() {
        return palpiteService.findAll();
    }

    @GetMapping("/{id}")
    public PalpiteResponse findById(@PathVariable UUID id) {
        return palpiteService.findById(id);
    }

    @PutMapping("/{id}")
    public PalpiteResponse update(
        @PathVariable UUID id,
        @AuthenticationPrincipal UserEntity currentUser,
        @Valid @RequestBody PalpiteRequest request
    ) {
        return palpiteService.update(id, currentUser.getId(), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal UserEntity currentUser) {
        palpiteService.delete(id, currentUser.getId());
    }
}

