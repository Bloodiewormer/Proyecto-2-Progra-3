package org.example.API;
// package org.example.API.Controllers;

import org.example.Application.Services.RecetaService;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Domain.dtos.Receta.UpdateRecetaRequestDto;
import org.example.Utilities.EstadoReceta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despacho")
@CrossOrigin(origins = "*") // Permite que el FrontEnd acceda desde cualquier origen
public class DespachoController {

    private final RecetaService recetaService;

    public DespachoController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /**
     * Obtener todas las recetas
     */
    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaResponseDto>> obtenerRecetas() {
        List<RecetaResponseDto> recetas = recetaService.obtenerTodas();
        return ResponseEntity.ok(recetas);
    }

    /**
     * Obtener recetas filtradas por estado
     */
    @GetMapping("/recetas/estado/{estado}")
    public ResponseEntity<List<RecetaResponseDto>> obtenerRecetasPorEstado(@PathVariable EstadoReceta estado) {
        List<RecetaResponseDto> recetas = recetaService.obtenerPorEstado(estado.name());
        return ResponseEntity.ok(recetas);
    }

    /**
     * Obtener recetas por paciente
     */
    @GetMapping("/recetas/paciente/{idPaciente}")
    public ResponseEntity<List<RecetaResponseDto>> obtenerRecetasPorPaciente(@PathVariable int idPaciente) {
        List<RecetaResponseDto> recetas = recetaService.obtenerPorPaciente(idPaciente);
        return ResponseEntity.ok(recetas);
    }

    /**
     * Actualizar el estado de una receta
     */
    @PutMapping("/recetas/{id}/estado")
    public ResponseEntity<String> actualizarEstadoReceta(
            @PathVariable int id,
            @RequestBody UpdateRecetaRequestDto requestDto) {

        boolean actualizado = recetaService.actualizarEstado(id, requestDto.getNuevoEstado());

        if (actualizado) {
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo actualizar la receta.");
        }
    }
}
