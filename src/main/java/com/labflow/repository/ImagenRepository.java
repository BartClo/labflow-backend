package com.labflow.repository;

import com.labflow.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, UUID> {

    Optional<Imagen> findByMuestraIdMuestra(UUID muestraId);

    boolean existsByMuestraIdMuestra(UUID muestraId);

    void deleteByMuestraIdMuestra(UUID muestraId);
}
