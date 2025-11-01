package org.example.API;
// package org.example.API.Controllers;

import org.example.Application.Services.FarmaceutaService;
import org.example.Domain.Dtos.Farmaceuta.AddFarmaceutaRequestDto;
import org.example.Domain.Dtos.Farmaceuta.DeleteFarmaceutaRequestDto;
import org.example.Domain.Dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Domain.Dtos.Farmaceuta.UpdateFarmaceutaRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmaceutas")
@CrossOrigin(origins = "*") // permite que el FrontEnd Swing acceda al servidor
public class FarmaceutaController {

    private final FarmaceutaService farmaceutaService;

    public FarmaceutaController(FarmaceutaService farmaceutaService) {
        this.farmaceutaService = farmaceutaService;
    }

    /**
     * Obtener todos los farmaceutas
     */
    @GetMapping
    public ResponseEntity<List<FarmaceutaResponseDto>> listarFarmaceutas() {
        List<FarmaceutaResponseDto> farmaceutas = farmaceutaService.obtenerTodos();
        return ResponseEntity.ok(farmaceutas);
    }

    /**
     * Agregar un nuevo farmaceuta
     */
    @PostMapping
    public ResponseEntity<FarmaceutaResponseDto> agregarFarmaceuta(@RequestBody AddFarmaceutaRequestDto request) {
        FarmaceutaResponseDto nuevo = farmaceutaService.agregar(request);
        return ResponseEntity.ok(nuevo);
    }

    /**
     * Actualizar un farmaceuta existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmaceutaResponseDto> actualizarFarmaceuta(
            @PathVariable int id,
            @RequestBody UpdateFarmaceutaRequestDto request
    ) {
        request.setId(id);
        FarmaceutaResponseDto actualizado = farmaceutaService.actualizar(request);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Eliminar un farmaceuta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminarFarmaceuta(@PathVariable int id) {
        DeleteFarmaceutaRequestDto dto = new DeleteFarmaceutaRequestDto(id);
        boolean eliminado = farmaceutaService.eliminar(dto);
        return ResponseEntity.ok(eliminado);
    }

    /**
     * Buscar farmaceutas por nombre (opcional)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<FarmaceutaResponseDto>> buscarPorNombre(@RequestParam String nombre) {
        List<FarmaceutaResponseDto> resultado = farmaceutaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultado);
    }
}

// Revisar