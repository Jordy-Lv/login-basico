package com.empresaxyz.loginbasico.service;

import com.empresaxyz.loginbasico.dto.ConductorRequest;
import com.empresaxyz.loginbasico.exception.ResourceNotFoundException;
import com.empresaxyz.loginbasico.model.Conductor;
import com.empresaxyz.loginbasico.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;

    public List<Conductor> listarTodos() {
        return conductorRepository.findAll();
    }

    public Conductor buscarPorId(Long id) {
        return conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con id: " + id));
    }

    public Conductor crear(ConductorRequest request) {
        Conductor conductor = new Conductor();
        conductor.setNombre(request.getNombre());
        conductor.setDocumento(request.getDocumento());
        conductor.setLicencia(request.getLicencia());
        return conductorRepository.save(conductor);
    }

    public Conductor actualizar(Long id, ConductorRequest request) {
        Conductor conductor = buscarPorId(id);
        conductor.setNombre(request.getNombre());
        conductor.setDocumento(request.getDocumento());
        conductor.setLicencia(request.getLicencia());
        return conductorRepository.save(conductor);
    }

    public void eliminar(Long id) {
        Conductor conductor = buscarPorId(id);
        conductorRepository.delete(conductor);
    }
}
