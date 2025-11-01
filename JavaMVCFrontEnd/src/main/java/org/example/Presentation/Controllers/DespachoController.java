package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Receta.UpdateRecetaRequestDto;
import org.example.Utilities.EstadoReceta;

import java.util.List;
import java.util.stream.Collectors;

public class DespachoController {
    private final List<RecetaResponseDto> recetas;

    public DespachoController(List<RecetaResponseDto> recetas) {
        this.recetas = recetas != null ? recetas : List.of();
    }

    public List<RecetaResponseDto> obtenerRecetas() {
        return recetas;
    }

    public void actualizarEstadoReceta(int recetaId, EstadoReceta nuevoEstado) {
        // Esta operación debería hacerse a través de un servicio
        // Por ahora solo actualizamos localmente
        RecetaResponseDto receta = recetas.stream()
                .filter(r -> r.getId() == recetaId)
                .findFirst()
                .orElse(null);

        if (receta != null) {
            receta.setEstado(nuevoEstado.name());
        }
    }

    public List<RecetaResponseDto> obtenerRecetasPorEstado(EstadoReceta estado) {
        return recetas.stream()
                .filter(r -> estado.name().equals(r.getEstado()))
                .collect(Collectors.toList());
    }

    public List<RecetaResponseDto> obtenerRecetasPorPaciente(int idPaciente) {
        return recetas.stream()
                .filter(r -> r.getIdPaciente() == idPaciente)
                .collect(Collectors.toList());
    }
}