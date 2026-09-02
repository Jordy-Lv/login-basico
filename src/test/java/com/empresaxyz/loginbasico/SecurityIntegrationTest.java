package com.empresaxyz.loginbasico;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.empresaxyz.loginbasico.TokenTestHelper.bearer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sinToken_todoResponde401() throws Exception {
        mockMvc.perform(get("/api/camiones")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/conductores")).andExpect(status().isUnauthorized());
    }

    @Test
    void loginConCredencialesValidas_devuelveToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void loginConCredencialesInvalidas_responde401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"password-incorrecto\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenMalFormado_responde401() throws Exception {
        mockMvc.perform(get("/api/camiones").header(HttpHeaders.AUTHORIZATION, "Bearer abc.def.ghi"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void usuarioAutenticado_puedeConsultarCamiones() throws Exception {
        mockMvc.perform(get("/api/camiones")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123")))
                .andExpect(status().isOk());
    }

    @Test
    void supervisor_recibe403AlCrearCamion() throws Exception {
        mockMvc.perform(post("/api/camiones")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"XYZ111\",\"tipoVehiculo\":\"Furgon\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_puedeCrearCamion() throws Exception {
        mockMvc.perform(post("/api/camiones")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"XYZ222\",\"tipoVehiculo\":\"Furgon\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void admin_puedeCrearConductor() throws Exception {
        mockMvc.perform(post("/api/conductores")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan Perez\",\"documento\":\"999888777\",\"licencia\":\"C2-111\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void supervisor_recibe403AlCrearConductor() throws Exception {
        mockMvc.perform(post("/api/conductores")
                        .header(HttpHeaders.AUTHORIZATION, bearer(mockMvc, "supervisor", "supervisor123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan Perez\",\"documento\":\"999888666\",\"licencia\":\"C2-112\"}"))
                .andExpect(status().isForbidden());
    }
}
