package com.empresaxyz.loginbasico.repository;

import com.empresaxyz.loginbasico.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    boolean existsByDocumento(String documento);
}
