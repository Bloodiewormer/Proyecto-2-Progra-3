package org.example.API;
// package org.example.API.Controllers;

import org.example.Domain.dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.dtos.Receta.UpdateRecetaRequestDto;
import org.example.Domain.dtos.Receta.DeleteRecetaRequestDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Services.Interfaces.IRecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador API encargado de exponer endpoints para la gestión de recetas médicas.
 * Este controlador es consumido directamente por el RecetaController del FrontEnd.
 */
@RestController
@RequestMapping("/api/recetas")
@CrossOrigin(origins = "*")
public class RecetaController {

    private final IRecetaService recetaService;

    @Autowired
    public RecetaController(IRecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /**
     * Crear una nueva receta médica
     */
    @PostMapping("/agregar")
    public ResponseEntity<RecetaResponseDto> agregarReceta(@RequestBody AddRecetaRequestDto request) {
        try {
            RecetaResponseDto receta = recetaService.agregarReceta(request);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Actualizar una receta existente
     */
    @PutMapping("/actualizar")
    public ResponseEntity<RecetaResponseDto> actualizarReceta(@RequestBody UpdateRecetaRequestDto request) {
        try {
            RecetaResponseDto receta = recetaService.actualizarReceta(request);
            return receta != null
                    ? ResponseEntity.ok(receta)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Eliminar una receta
     */
    @DeleteMapping("/eliminar")
    public ResponseEntity<Boolean> eliminarReceta(@RequestBody DeleteRecetaRequestDto request) {
        try {
            boolean eliminado = recetaService.eliminarReceta(request.getId());
            return ResponseEntity.ok(eliminado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(false);
        }
    }

    /**
     * Obtener la lista de todas las recetas registradas
     */
    @GetMapping("/listar")
    public ResponseEntity<List<RecetaResponseDto>> listarRecetas() {
        try {
            List<RecetaResponseDto> recetas = recetaService.listarRecetas();
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtener una receta específica por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDto> obtenerPorId(@PathVariable int id) {
        try {
            RecetaResponseDto receta = recetaService.obtenerPorId(id);
            return receta != null
                    ? ResponseEntity.ok(receta)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

// Revisar