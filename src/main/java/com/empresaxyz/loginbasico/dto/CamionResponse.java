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

    public static CamionResponse desde(Camion camion) {
        return new CamionResponse(camion.getId(), camion.getPlaca(), camion.getTipoVehiculo());
    }
}
