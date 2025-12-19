package com.labflow.repository;

import com.labflow.model.Muestra;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Specifications para búsqueda dinámica de muestras
 * Permite construir consultas con criterios opcionales
 */
public class MuestraSpecification {

    public static Specification<Muestra> buscarPorCriterios(
            UUID clienteId,
            Muestra.EstadoMuestra estado,
            Muestra.Prioridad prioridad,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrar por cliente si se proporciona
            if (clienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cliente").get("idCliente"), clienteId));
            }

            // Filtrar por estado si se proporciona
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            // Filtrar por prioridad si se proporciona
            if (prioridad != null) {
                predicates.add(criteriaBuilder.equal(root.get("prioridad"), prioridad));
            }

            // Filtrar por fecha de inicio si se proporciona
            if (fechaInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaMuestreo"), fechaInicio));
            }

            // Filtrar por fecha de fin si se proporciona
            if (fechaFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaMuestreo"), fechaFin));
            }

            // Si no hay predicados, retornar todos los registros
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
