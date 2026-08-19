package com.empresaxyz.loginbasico.service;

import com.empresaxyz.loginbasico.dto.CamionRequest;
import com.empresaxyz.loginbasico.exception.ResourceNotFoundException;
import com.empresaxyz.loginbasico.model.Camion;
import com.empresaxyz.loginbasico.model.Conductor;
import com.empresaxyz.loginbasico.repository.CamionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;
    private final ConductorService conductorService;

    public List<Camion> listarTodos() {
        return camionRepository.findAll();
    }

    public Camion buscarPorId(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camion no encontrado con id: " + id));
    }

    public Camion crear(CamionRequest request) {
        Camion camion = new Camion();
        camion.setPlaca(request.getPlaca());
        camion.setTipoVehiculo(request.getTipoVehiculo());
        return camionRepository.save(camion);
    }

    public Camion actualizar(Long id, CamionRequest request) {
        Camion camion = buscarPorId(id);
        camion.setPlaca(request.getPlaca());
        camion.setTipoVehiculo(request.getTipoVehiculo());
        return camionRepository.save(camion);
    }

    public void eliminar(Long id) {
        Camion camion = buscarPorId(id);
        camionRepository.delete(camion);
    }

    public Camion asociarConductor(Long camionId, Long conductorId) {
        Camion camion = buscarPorId(camionId);
        Conductor conductor = conductorService.buscarPorId(conductorId);

        // Un conductor solo puede estar asociado a un camion a la vez: si ya estaba
        // asignado a otro, se libera ese camion antes de la nueva asociacion.
        camionRepository.findByConductorId(conductorId)
                .filter(otro -> !otro.getId().equals(camionId))
                .ifPresent(otro -> {
                    otro.setConductor(null);
                    camionRepository.save(otro);
                });

        camion.setConductor(conductor);
        return camionRepository.save(camion);
    }

    public Camion desasociarConductor(Long camionId) {
        Camion camion = buscarPorId(camionId);
        camion.setConductor(null);
        return camionRepository.save(camion);
    }
}
