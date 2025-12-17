package com.labflow.config;

import com.labflow.model.Client;
import com.labflow.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Crea un cliente semilla si la base de datos no tiene clientes aún.
 */
@Component
public class ClientSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ClientSeeder.class);
    private static final String NOMBRE_SEMILLA = "Cliente Semilla";

    private final ClientRepository clientRepository;

    public ClientSeeder(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        if (clientRepository.count() > 0) {
            log.info("ℹ️ La base de datos ya tiene clientes, se omite cliente semilla.");
            return;
        }

        // Evitar duplicado si el seeder se ejecuta múltiples veces en entornos diferentes
        if (clientRepository.findByNombreCliente(NOMBRE_SEMILLA).isPresent()) {
            log.info("ℹ️ Cliente semilla ya existe, sin crear duplicados.");
            return;
        }

        Client cliente = new Client();
        cliente.setNombreCliente(NOMBRE_SEMILLA);
        cliente.setNombre("Cliente Demo");
        cliente.setEmpresa("Labflow Demo");
        cliente.setEmail("demo@labflow.local");
        cliente.setTelefono("000000000");
        cliente.setDireccion("Dirección semilla");
        cliente.setPersonaContacto("Contacto Semilla");
        cliente.setTipoCliente("Empresa");
        cliente.setActivo(true);

        Client guardado = clientRepository.save(cliente);
        log.info("✅ Cliente semilla creado con ID {}", guardado.getIdCliente());
    }
}
