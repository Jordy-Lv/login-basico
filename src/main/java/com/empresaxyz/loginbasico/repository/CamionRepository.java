package com.empresaxyz.loginbasico.repository;

import com.empresaxyz.loginbasico.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByPlaca(String placa);
    Optional<Camion> findByConductorId(Long conductorId);
}
