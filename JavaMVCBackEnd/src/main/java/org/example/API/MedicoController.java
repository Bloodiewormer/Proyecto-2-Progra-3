package org.example.API;
// package org.example.API.Controllers;

import org.example.Domain.dtos.Medico.AddMedicoRequestDto;
import org.example.Domain.dtos.Medico.UpdateMedicoRequestDto;
import org.example.Domain.dtos.Medico.DeleteMedicoRequestDto;
import org.example.Domain.dtos.Medico.MedicoResponseDto;
import org.example.Services.MedicoService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*") // Permite acceso desde tu FrontEnd (Swing o web)
public class MedicoController {

    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // ✅ Obtener todos los médicos
    @GetMapping
    public ResponseEntity<List<MedicoResponseDto>> getAllMedicos() {
        List<MedicoResponseDto> medicos = medicoService.getAllMedicos();
        return ResponseEntity.ok(medicos);
    }

    // ✅ Agregar nuevo médico
    @PostMapping
    public ResponseEntity<MedicoResponseDto> addMedico(@RequestBody AddMedicoRequestDto request) {
        MedicoResponseDto created = medicoService.addMedico(request);
        return ResponseEntity.ok(created);
    }

    // ✅ Actualizar médico existente
    @PutMapping
    public ResponseEntity<MedicoResponseDto> updateMedico(@RequestBody UpdateMedicoRequestDto request) {
        MedicoResponseDto updated = medicoService.updateMedico(request);
        return ResponseEntity.ok(updated);
    }

    // ✅ Eliminar médico
    @DeleteMapping
    public ResponseEntity<Boolean> deleteMedico(@RequestBody DeleteMedicoRequestDto request) {
        boolean deleted = medicoService.deleteMedico(request.getId());
        return ResponseEntity.ok(deleted);
    }

    // ✅ Buscar médico por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDto> getMedicoById(@PathVariable int id) {
        MedicoResponseDto medico = medicoService.getMedicoById(id);
        return medico != null ? ResponseEntity.ok(medico) : ResponseEntity.notFound().build();
    }

    // ✅ Buscar médico por nombre o especialidad
    @GetMapping("/buscar")
    public ResponseEntity<List<MedicoResponseDto>> searchMedicos(@RequestParam("filtro") String filtro) {
        List<MedicoResponseDto> result = medicoService.searchMedicos(filtro);
        return ResponseEntity.ok(result);
    }
}
