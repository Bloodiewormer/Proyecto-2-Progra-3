package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;

import java.util.List;
import java.util.stream.Collectors;

public class HistorialRecetaController {

    private final PacienteService pacienteService;
    private final RecetaService recetaService;

    public HistorialRecetaController(PacienteService pacienteService, RecetaService recetaService) {
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
                .filter(r -> r.getPaciente().getId() == paciente.getId())
                .collect(Collectors.toList());
    }
}
