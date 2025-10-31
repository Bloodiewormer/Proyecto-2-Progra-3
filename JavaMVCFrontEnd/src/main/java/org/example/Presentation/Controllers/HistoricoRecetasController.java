package org.example.Presentation.Controllers;

import org.example.Services.PacienteService;
import org.example.Services.RecetaService;

import java.util.List;
import java.util.stream.Collectors;

public class HistoricoRecetasController {

    private final PacienteService pacienteService;
    private final RecetaService recetaService;

    public HistoricoRecetasController(PacienteService pacienteService, RecetaService recetaService) {
        this.pacienteService = pacienteService;
        this.recetaService = recetaService;
    }

    public Paciente buscarPacientePorId(int id) {
        return pacienteService.leerPorId(id);
    }

    public List<Paciente> buscarPacientePorNombre(String nombre) {
        return pacienteService.leerTodos()
                .stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Receta> obtenerHistorialRecetas(Paciente paciente) {
        return recetaService.leerTodos()
                .stream()
                .filter(r -> r.getIdPaciente() == paciente.getId())
                .collect(Collectors.toList());
    }

    public List<Receta> obtenerRecetasPorPaciente(Integer pacienteSeleccionadoId) {
        return recetaService.buscarPorPacienteId(pacienteSeleccionadoId);
    }

    public List<Receta> obtenerRecetas() {
        return recetaService.leerTodos();
    }
}
