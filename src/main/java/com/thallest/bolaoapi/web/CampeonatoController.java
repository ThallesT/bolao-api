package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.service.CampeonatoService;
import com.thallest.bolaoapi.web.dto.CampeonatoRequest;
import com.thallest.bolaoapi.web.dto.CampeonatoResponse;
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
@RequestMapping("/championships")
public class CampeonatoController {

    private final CampeonatoService campeonatoService;

    public CampeonatoController(CampeonatoService campeonatoService) {
        this.campeonatoService = campeonatoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampeonatoResponse create(@Valid @RequestBody CampeonatoRequest request) {
        return campeonatoService.create(request);
    }

    @GetMapping
    public List<CampeonatoResponse> findAll() {
        return campeonatoService.findAll();
    }

    @GetMapping("/{id}")
    public CampeonatoResponse findById(@PathVariable Long id) {
        return campeonatoService.findById(id);
    }

    @PutMapping("/{id}")
    public CampeonatoResponse update(@PathVariable Long id, @Valid @RequestBody CampeonatoRequest request) {
        return campeonatoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        campeonatoService.delete(id);
    }
}

