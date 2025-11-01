package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class HistoricoRecetasController {

    private final List<PacienteResponseDto> pacientes;
    private final List<RecetaResponseDto> recetas;

    public HistoricoRecetasController(List<PacienteResponseDto> pacientes, List<RecetaResponseDto> recetas) {
        this.pacientes = pacientes != null ? pacientes : List.of();
        this.recetas = recetas != null ? recetas : List.of();
    }

    public PacienteResponseDto buscarPacientePorId(int id) {
        return pacientes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<PacienteResponseDto> buscarPacientePorNombre(String nombre) {
        return pacientes.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<RecetaResponseDto> obtenerHistorialRecetas(PacienteResponseDto paciente) {
        if (paciente == null) return List.of();

        return recetas.stream()
                .filter(r -> r.getIdPaciente() == paciente.getId())
                .collect(Collectors.toList());
    }

    public List<RecetaResponseDto> obtenerRecetasPorPaciente(Integer pacienteId) {
        if (pacienteId == null) return List.of();

        return recetas.stream()
                .filter(r -> r.getIdPaciente() == pacienteId)
                .collect(Collectors.toList());
    }

    public List<RecetaResponseDto> obtenerRecetas() {
        return recetas;
    }
}