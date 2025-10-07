package com.labflow.repository;

import com.labflow.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones CRUD de la entidad Client
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Buscar cliente por nombre exacto
     */
    Optional<Client> findByNombreCliente(String nombreCliente);

    /**
     * Buscar clientes por nombre que contenga el texto especificado (case insensitive)
     */
    @Query("SELECT c FROM Client c WHERE LOWER(c.nombreCliente) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Client> findByNombreClienteContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Verificar si existe un cliente con el nombre especificado
     */
    boolean existsByNombreCliente(String nombreCliente);

    /**
     * Contar total de clientes
     */
    @Query("SELECT COUNT(c) FROM Client c")
    long countTotalClients();

    /**
     * Obtener los últimos clientes creados
     */
    @Query("SELECT c FROM Client c ORDER BY c.fechaCreacion DESC")
    List<Client> findLatestClients();
}