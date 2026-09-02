package com.empresaxyz.loginbasico;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Hace login contra /api/auth/login y devuelve el header Authorization listo para usar.
 */
final class TokenTestHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TokenTestHelper() {
    }

    static String bearer(MockMvc mockMvc, String username, String password) throws Exception {
        return "Bearer " + token(mockMvc, username, password);
    }

    static String token(MockMvc mockMvc, String username, String password) throws Exception {
        String cuerpo = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        String respuesta = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return MAPPER.readTree(respuesta).get("token").asText();
    }
}
