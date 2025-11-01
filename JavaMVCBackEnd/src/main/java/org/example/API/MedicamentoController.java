package org.example.API;
// package org.example.API.Controllers;

import org.example.Application.Services.MedicamentoService;
import org.example.Domain.Dtos.Medicamento.*;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*") // Permite conexión desde tu FrontEnd
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    /**
     * Obtener todos los medicamentos
     */
    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDto>> getAllMedicamentos() {
        List<MedicamentoResponseDto> medicamentos = medicamentoService.getAllMedicamentos();
        return ResponseEntity.ok(medicamentos);
    }

    /**
     * Agregar un nuevo medicamento
     */
    @PostMapping
    public ResponseEntity<MedicamentoResponseDto> addMedicamento(@RequestBody AddMedicamentoRequestDto dto) {
        MedicamentoResponseDto created = medicamentoService.addMedicamento(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * Actualizar un medicamento existente
     */
    @PutMapping
    public ResponseEntity<MedicamentoResponseDto> updateMedicamento(@RequestBody UpdateMedicamentoRequestDto dto) {
        MedicamentoResponseDto updated = medicamentoService.updateMedicamento(dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar un medicamento por ID
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteMedicamento(@RequestBody DeleteMedicamentoRequestDto dto) {
        boolean success = medicamentoService.deleteMedicamento(dto);
        return ResponseEntity.ok(success);
    }
}

// Revisar