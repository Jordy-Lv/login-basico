package com.empresaxyz.loginbasico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "camiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(name = "tipo_vehiculo", nullable = false)
    private String tipoVehiculo;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

}
