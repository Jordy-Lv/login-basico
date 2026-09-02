package com.empresaxyz.loginbasico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tipo;
    private long expiraEnMs;

    public static LoginResponse desde(String token, long expiraEnMs) {
        return new LoginResponse(token, "Bearer", expiraEnMs);
    }
}
