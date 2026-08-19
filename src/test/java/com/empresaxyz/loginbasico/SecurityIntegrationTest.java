package com.empresaxyz.loginbasico;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sinCredenciales_todoResponde401() throws Exception {
        mockMvc.perform(get("/api/camiones")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/conductores")).andExpect(status().isUnauthorized());
    }

    @Test
    void credencialesInvalidas_responde401() throws Exception {
        mockMvc.perform(get("/api/camiones").with(httpBasic("admin", "password-incorrecto")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void usuarioAutenticado_puedeConsultarCamiones() throws Exception {
        mockMvc.perform(get("/api/camiones").with(httpBasic("supervisor", "supervisor123")))
                .andExpect(status().isOk());
    }

    @Test
    void supervisor_recibe403AlCrearCamion() throws Exception {
        mockMvc.perform(post("/api/camiones")
                        .with(httpBasic("supervisor", "supervisor123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"XYZ111\",\"tipoVehiculo\":\"Furgon\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_puedeCrearCamion() throws Exception {
        mockMvc.perform(post("/api/camiones")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"XYZ222\",\"tipoVehiculo\":\"Furgon\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void admin_puedeCrearConductor() throws Exception {
        mockMvc.perform(post("/api/conductores")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan Perez\",\"documento\":\"999888777\",\"licencia\":\"C2-111\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void supervisor_recibe403AlCrearConductor() throws Exception {
        mockMvc.perform(post("/api/conductores")
                        .with(httpBasic("supervisor", "supervisor123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan Perez\",\"documento\":\"999888666\",\"licencia\":\"C2-112\"}"))
                .andExpect(status().isForbidden());
    }
}
