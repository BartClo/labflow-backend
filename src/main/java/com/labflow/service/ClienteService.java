package com.labflow.service;

import com.labflow.dto.ClienteCreateDTO;
import com.labflow.dto.ClienteDTO;
import com.labflow.model.Client;
import com.labflow.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para gestionar operaciones CRUD de clientes
 */
@Service
@Transactional
public class ClienteService {

    private final ClientRepository clientRepository;

    public ClienteService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Crear un nuevo cliente
     */
    public ClienteDTO crearCliente(ClienteCreateDTO clienteCreateDTO) {
        Client client = new Client();
        client.setNombreCliente(clienteCreateDTO.getNombreCliente());
        
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }

    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodosLosClientes() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Obtener cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorId(UUID id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(this::convertToDTO);
    }

    /**
     * Actualizar cliente
     */
    public Optional<ClienteDTO> actualizarCliente(UUID id, ClienteCreateDTO clienteCreateDTO) {
        Optional<Client> existingClient = clientRepository.findById(id);
        
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            client.setNombreCliente(clienteCreateDTO.getNombreCliente());
            // No necesitamos setUpdatedAt manualmente, @UpdateTimestamp lo maneja
            
            Client updatedClient = clientRepository.save(client);
            return Optional.of(convertToDTO(updatedClient));
        }
        
        return Optional.empty();
    }

    /**
     * Eliminar cliente
     */
    public boolean eliminarCliente(UUID id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar clientes por nombre
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> buscarClientesPorNombre(String nombre) {
        List<Client> clients = clientRepository.findByNombreClienteContainingIgnoreCase(nombre);
        return clients.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Convertir entidad a DTO
     */
    private ClienteDTO convertToDTO(Client client) {
        return new ClienteDTO(
                client.getIdCliente(),
                client.getNombreCliente(),
                client.getFechaCreacion(),
                client.getFechaActualizacion()
        );
    }
}