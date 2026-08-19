package com.empresaxyz.loginbasico.repository;

import com.empresaxyz.loginbasico.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByPlaca(String placa);
}
