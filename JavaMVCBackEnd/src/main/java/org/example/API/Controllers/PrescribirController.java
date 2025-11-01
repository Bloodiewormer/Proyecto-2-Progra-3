package org.example.API.Controllers;
// package org.example.API.Controllers;

import org.example.Domain.dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Services.Interfaces.IRecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador API que gestiona la prescripción de recetas médicas.
 * Este controlador es consumido por el PrescribirController del FrontEnd.
 */
@RestController
@RequestMapping("/api/prescripciones")
@CrossOrigin(origins = "*")
public class PrescribirController {

    private final IRecetaService recetaService;

    @Autowired
    public PrescribirController(IRecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /**
     * Endpoint para registrar una nueva receta médica con sus detalles.
     *
     * @param request DTO que contiene los datos del paciente, médico y detalles de la receta.
     * @return La receta registrada, incluyendo su ID generado y los detalles asociados.
     */
    @PostMapping("/registrar")
    public ResponseEntity<RecetaResponseDto> registrarReceta(@RequestBody AddRecetaRequestDto request) {
        try {
            RecetaResponseDto receta = recetaService.registrarReceta(request);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint opcional: obtiene una receta específica por su ID.
     *
     * @param id Identificador de la receta.
     * @return La receta encontrada o 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDto> obtenerPorId(@PathVariable int id) {
        RecetaResponseDto receta = recetaService.obtenerPorId(id);
        return receta != null
                ? ResponseEntity.ok(receta)
                : ResponseEntity.notFound().build();
    }
}

// Revisar