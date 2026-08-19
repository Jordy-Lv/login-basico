package com.empresaxyz.loginbasico.controller;

import com.empresaxyz.loginbasico.dto.CamionRequest;
import com.empresaxyz.loginbasico.dto.CamionResponse;
import com.empresaxyz.loginbasico.model.Camion;
import com.empresaxyz.loginbasico.service.CamionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
public class CamionController {

    private final CamionService camionService;

    @GetMapping
    public List<CamionResponse> listar() {
        return camionService.listarTodos().stream().map(CamionResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public CamionResponse obtener(@PathVariable Long id) {
        return CamionResponse.desde(camionService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CamionResponse> crear(@Valid @RequestBody CamionRequest request) {
        Camion camion = camionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CamionResponse.desde(camion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CamionResponse actualizar(@PathVariable Long id, @Valid @RequestBody CamionRequest request) {
        return CamionResponse.desde(camionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        camionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{camionId}/conductores/{conductorId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public CamionResponse asociarConductor(@PathVariable Long camionId, @PathVariable Long conductorId) {
        return CamionResponse.desde(camionService.asociarConductor(camionId, conductorId));
    }

    @DeleteMapping("/{camionId}/conductores")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public CamionResponse desasociarConductor(@PathVariable Long camionId) {
        return CamionResponse.desde(camionService.desasociarConductor(camionId));
    }
}
