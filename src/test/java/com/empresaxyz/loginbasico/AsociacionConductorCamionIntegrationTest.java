package com.empresaxyz.loginbasico;

import com.empresaxyz.loginbasico.model.Camion;
import com.empresaxyz.loginbasico.model.Conductor;
import com.empresaxyz.loginbasico.repository.CamionRepository;
import com.empresaxyz.loginbasico.repository.ConductorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.empresaxyz.loginbasico.TokenTestHelper.bearer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AsociacionConductorCamionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CamionRepository camionRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    private Long camion1Id;
    private Long camion2Id;
    private Long conductorId;

    @BeforeEach
    void crearDatosDePrueba() {
        Camion camion1 = new Camion();
        camion1.setPlaca("AAA111");
        camion1.setTipoVehiculo("Furgon refrigerado");
        camion1Id = camionRepository.save(camion1).getId();

        Camion camion2 = new Camion();
        camion2.setPlaca("BBB222");
        camion2.setTipoVehiculo("Furgon refrigerado");
        camion2Id = camionRepository.save(camion2).getId();

        Conductor conductor = new Conductor();
        conductor.setNombre("Juan Perez");
        conductor.setDocumento("123123123");
        conductor.setLicencia("C2-999");
        conductorId = conductorRepository.save(conductor).getId();
    }

    @Test
    void sinToken_asociarResponde401() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/" + conductorId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void supervisor_puedeAsociarConductorACamion() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conductorId").value(conductorId))
                .andExpect(jsonPath("$.conductorNombre").value("Juan Perez"));
    }

    @Test
    void admin_puedeAsociarConductorACamion() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conductorId").value(conductorId));
    }

    @Test
    void asociarACamionInexistente_responde404() throws Exception {
        mockMvc.perform(post("/api/camiones/99999/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123")))
                .andExpect(status().isNotFound());
    }

    @Test
    void asociarConductorInexistente_responde404() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/99999")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123")))
                .andExpect(status().isNotFound());
    }

    @Test
    void reasociarConductorAOtroCamion_loLiberaDelPrimero() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/camiones/" + camion2Id + "/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conductorId").value(conductorId));

        Camion camion1Actualizado = camionRepository.findById(camion1Id).orElseThrow();
        org.junit.jupiter.api.Assertions.assertNull(camion1Actualizado.getConductor());
    }

    @Test
    void desasociarConductor_dejaCamionSinConductor() throws Exception {
        mockMvc.perform(post("/api/camiones/" + camion1Id + "/conductores/" + conductorId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123")))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/camiones/" + camion1Id + "/conductores")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conductorId").value(nullValue()));
    }
}
