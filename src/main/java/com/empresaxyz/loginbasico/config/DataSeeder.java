package com.empresaxyz.loginbasico.config;

import com.empresaxyz.loginbasico.model.Rol;
import com.empresaxyz.loginbasico.model.Usuario;
import com.empresaxyz.loginbasico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);

            Usuario supervisor = new Usuario();
            supervisor.setUsername("supervisor");
            supervisor.setPassword(passwordEncoder.encode("supervisor123"));
            supervisor.setRol(Rol.SUPERVISOR);
            usuarioRepository.save(supervisor);
        }
    }
}
