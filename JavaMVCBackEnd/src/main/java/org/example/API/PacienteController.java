package org.example.API;

import org.example.Domain.dtos.Paciente.*;
import org.example.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*") // Permite comunicación desde el FrontEnd
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // ✅ Obtener todos los pacientes
    @GetMapping
    public ResponseEntity<List<PacienteResponseDto>> getAllPacientes() {
        List<PacienteResponseDto> pacientes = pacienteService.getAllPacientes();
        return ResponseEntity.ok(pacientes);
    }

    // ✅ Obtener paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDto> getPacienteById(@PathVariable int id) {
        PacienteResponseDto paciente = pacienteService.getPacienteById(id);
        return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
    }

    // ✅ Agregar nuevo paciente
    @PostMapping
    public ResponseEntity<PacienteResponseDto> addPaciente(@RequestBody AddPacienteRequestDto request) {
        PacienteResponseDto created = pacienteService.addPaciente(request);
        return ResponseEntity.ok(created);
    }

    // ✅ Actualizar paciente
    @PutMapping
    public ResponseEntity<PacienteResponseDto> updatePaciente(@RequestBody UpdatePacienteRequestDto request) {
        PacienteResponseDto updated = pacienteService.updatePaciente(request);
        return ResponseEntity.ok(updated);
    }

    // ✅ Eliminar paciente
    @DeleteMapping
    public ResponseEntity<Boolean> deletePaciente(@RequestBody DeletePacienteRequestDto request) {
        boolean deleted = pacienteService.deletePaciente(request.getId());
        return ResponseEntity.ok(deleted);
    }

    // ✅ Buscar pacientes por nombre o teléfono
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteResponseDto>> searchPacientes(@RequestParam("filtro") String filtro) {
        List<PacienteResponseDto> resultados = pacienteService.searchPacientes(filtro);
        return ResponseEntity.ok(resultados);
    }
}

// Revisar