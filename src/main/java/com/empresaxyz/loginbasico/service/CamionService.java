package com.empresaxyz.loginbasico.service;

import com.empresaxyz.loginbasico.dto.CamionRequest;
import com.empresaxyz.loginbasico.exception.ResourceNotFoundException;
import com.empresaxyz.loginbasico.model.Camion;
import com.empresaxyz.loginbasico.repository.CamionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;

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
}
