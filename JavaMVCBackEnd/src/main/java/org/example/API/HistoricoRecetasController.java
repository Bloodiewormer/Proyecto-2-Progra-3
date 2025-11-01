package org.example.API;
// package org.example.API.Controllers;

import org.example.Application.Services.HistoricoRecetasService;
import org.example.Domain.dtos.Paciente.PacienteResponseDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico-recetas")
@CrossOrigin(origins = "*") // Permite que el FrontEnd (Swing) acceda
public class HistoricoRecetasController {

    private final HistoricoRecetasService historicoRecetasService;

    public HistoricoRecetasController(HistoricoRecetasService historicoRecetasService) {
        this.historicoRecetasService = historicoRecetasService;
    }

    /**
     * Buscar paciente por ID
     */
    @GetMapping("/paciente/{id}")
    public ResponseEntity<PacienteResponseDto> buscarPacientePorId(@PathVariable int id) {
        PacienteResponseDto paciente = historicoRecetasService.buscarPacientePorId(id);
        return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
    }

    /**
     * Buscar pacientes por nombre
     */
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteResponseDto>> buscarPacientesPorNombre(@RequestParam String nombre) {
        List<PacienteResponseDto> pacientes = historicoRecetasService.buscarPacientePorNombre(nombre);
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Obtener historial de recetas de un paciente específico
     */
    @GetMapping("/recetas/{pacienteId}")
    public ResponseEntity<List<RecetaResponseDto>> obtenerHistorialPorPaciente(@PathVariable int pacienteId) {
        List<RecetaResponseDto> recetas = historicoRecetasService.obtenerRecetasPorPaciente(pacienteId);
        return ResponseEntity.ok(recetas);
    }

    /**
     * Obtener todas las recetas registradas
     */
    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaResponseDto>> obtenerTodasLasRecetas() {
        List<RecetaResponseDto> recetas = historicoRecetasService.obtenerTodasLasRecetas();
        return ResponseEntity.ok(recetas);
    }
}
