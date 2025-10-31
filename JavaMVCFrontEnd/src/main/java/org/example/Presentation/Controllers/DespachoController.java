package org.example.Presentation.Controllers;

import org.example.Services.RecetaService;
import org.example.Services.MedicamentoService;
import org.example.Utilities.EstadoReceta;

import java.util.List;

public class DespachoController {
    private final RecetaService recetaService;
    private final MedicamentoService medicamentoService;

    public DespachoController(RecetaService recetaService, MedicamentoService medicamentoService) {
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
    }

    public List<Receta> obtenerRecetas() {
        return recetaService.leerTodos();
    }

    public void actualizarEstadoReceta(int recetaId, EstadoReceta nuevoEstado) {
        Receta receta = recetaService.leerPorId(recetaId);
        if (receta != null) {
            receta.setEstado(nuevoEstado);
            recetaService.actualizar(receta);
        }
    }

    public List<Receta> obtenerRecetasPorEstado(EstadoReceta estado) {
        return recetaService.leerTodos()
                .stream()
                .filter(r -> r.getEstadoEnum() == estado)
                .toList();
    }

    public List<Receta> obtenerRecetasPorPaciente(int idPaciente) {
        return recetaService.buscarPorPacienteId(idPaciente);
    }
}
