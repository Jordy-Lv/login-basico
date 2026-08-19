package com.empresaxyz.loginbasico.dto;

import com.empresaxyz.loginbasico.model.Camion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CamionResponse {

    private Long id;
    private String placa;
    private String tipoVehiculo;
    private Long conductorId;
    private String conductorNombre;

    public static CamionResponse desde(Camion camion) {
        Long conductorId = camion.getConductor() != null ? camion.getConductor().getId() : null;
        String conductorNombre = camion.getConductor() != null ? camion.getConductor().getNombre() : null;
        return new CamionResponse(camion.getId(), camion.getPlaca(), camion.getTipoVehiculo(), conductorId, conductorNombre);
    }
}
