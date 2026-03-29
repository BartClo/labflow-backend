package com.labflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@Tag(name = "Quotes/Cotizaciones", description = "API para gestión de cotizaciones")
public class CotizacionController {

    @GetMapping
    @Operation(summary = "Listar todas las cotizaciones", description = "Obtiene un listado de todas las cotizaciones del sistema")
    public ResponseEntity<List<?>> listarTodos() {
        // Return empty list for now - quotes functionality not yet implemented
        return ResponseEntity.ok(new ArrayList<>());
    }
}
