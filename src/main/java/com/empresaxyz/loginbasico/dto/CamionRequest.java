package com.empresaxyz.loginbasico.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CamionRequest {

    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    @NotBlank(message = "El tipo de vehiculo es obligatorio")
    private String tipoVehiculo;

}
