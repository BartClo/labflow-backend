package com.labflow.config;

import com.labflow.model.Rol;
import com.labflow.model.Usuario;
import com.labflow.repository.RolRepository;
import com.labflow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.email:admin@labflow.cl}")
    private String adminEmail;

    @Value("${admin.default.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Verificando existencia de usuario administrador...");

        // Usamos el rol ADMINISTRADOR
        Optional<Rol> rolAdminOpt = rolRepository.findByNombre("ADMINISTRADOR");
        if (rolAdminOpt.isEmpty()) {
            logger.warn("El rol ADMINISTRADOR no existe en la base de datos. Saltando seeding de admin.");
            return;
        }

        if (usuarioRepository.existsByEmail(adminEmail)) {
            logger.info("El usuario administrador ya existe.");
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombre("Administrador");
        admin.setApellido("Sistema");
        admin.setEmail(adminEmail);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setActivo(true);
        admin.setRol(rolAdminOpt.get());

        usuarioRepository.save(admin);
        logger.info("Usuario administrador creado exitosamente con el email: {}", adminEmail);
    }
}
