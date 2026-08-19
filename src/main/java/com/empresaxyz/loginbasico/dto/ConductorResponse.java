package com.empresaxyz.loginbasico.dto;

import com.empresaxyz.loginbasico.model.Conductor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConductorResponse {

    private Long id;
    private String nombre;
    private String documento;
    private String licencia;

    public static ConductorResponse desde(Conductor conductor) {
        return new ConductorResponse(conductor.getId(), conductor.getNombre(), conductor.getDocumento(), conductor.getLicencia());
    }
}
