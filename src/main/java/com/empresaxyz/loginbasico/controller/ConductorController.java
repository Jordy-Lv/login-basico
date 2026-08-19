package com.empresaxyz.loginbasico.controller;

import com.empresaxyz.loginbasico.dto.ConductorRequest;
import com.empresaxyz.loginbasico.dto.ConductorResponse;
import com.empresaxyz.loginbasico.model.Conductor;
import com.empresaxyz.loginbasico.service.ConductorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conductores")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @GetMapping
    public List<ConductorResponse> listar() {
        return conductorService.listarTodos().stream().map(ConductorResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public ConductorResponse obtener(@PathVariable Long id) {
        return ConductorResponse.desde(conductorService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConductorResponse> crear(@Valid @RequestBody ConductorRequest request) {
        Conductor conductor = conductorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConductorResponse.desde(conductor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ConductorResponse actualizar(@PathVariable Long id, @Valid @RequestBody ConductorRequest request) {
        return ConductorResponse.desde(conductorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        conductorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
