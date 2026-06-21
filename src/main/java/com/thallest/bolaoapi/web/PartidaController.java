package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.service.PartidaService;
import com.thallest.bolaoapi.web.dto.PartidaRequest;
import com.thallest.bolaoapi.web.dto.PartidaResponse;
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
@RequestMapping("/matches")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponse create(@Valid @RequestBody PartidaRequest request) {
        return partidaService.create(request);
    }

    @GetMapping
    public List<PartidaResponse> findAll() {
        return partidaService.findAll();
    }

    @GetMapping("/{id}")
    public PartidaResponse findById(@PathVariable Long id) {
        return partidaService.findById(id);
    }

    @PutMapping("/{id}")
    public PartidaResponse update(@PathVariable Long id, @Valid @RequestBody PartidaRequest request) {
        return partidaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        partidaService.delete(id);
    }
}

