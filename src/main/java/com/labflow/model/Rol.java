package com.labflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "permisos", columnDefinition = "TEXT[]")
    private String[] permisos;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor personalizado sin relaciones
    public Rol(String nombre, String descripcion, String[] permisos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permisos = permisos;
        this.activo = true;
    }

    // Métodos de utilidad
    public boolean tienePermiso(String permiso) {
        if (permisos == null) {
            return false;
        }
        for (String p : permisos) {
            if (p.equals(permiso)) {
                return true;
            }
        }
        return false;
    }

    @PrePersist
    protected void onCreate() {
        if (activo == null) {
            activo = true;
        }
    }
}
