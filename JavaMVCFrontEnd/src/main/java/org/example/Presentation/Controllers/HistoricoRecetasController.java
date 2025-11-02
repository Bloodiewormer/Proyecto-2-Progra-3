package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Presentation.Models.RecetaTableModel;
import org.example.Presentation.Views.HistoricoRecetasView;
import org.example.Services.PacienteService;
import org.example.Services.RecetaService;

import javax.swing.*;
import java.util.List;

public class HistoricoRecetasController {
    private final HistoricoRecetasView view;
    private final RecetaService recetaService;
    private final PacienteService pacienteService;
    private List<PacienteResponseDto> pacientes;

    public HistoricoRecetasController(HistoricoRecetasView view, RecetaService recetaService, PacienteService pacienteService) {
        this.view = view;
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
        inicializar();
    }

    private void inicializar() {
        cargarPacientes();
        cargarOpcionesBusqueda();
        view.getBuscarButton().addActionListener(e -> buscarRecetas());
        view.getDetallesButton().addActionListener(e -> mostrarDetalles());
        cargarRecetas();
    }

    private void cargarPacientes() {
        SwingWorker<List<PacienteResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PacienteResponseDto> doInBackground() throws Exception {
                return pacienteService.listPacientesAsync().get();
            }
            @Override
            protected void done() {
                try {
                    pacientes = get();
                } catch (Exception e) {
                    pacientes = List.of();
                }
            }
        };
        worker.execute();
    }

    private void cargarOpcionesBusqueda() {
        JComboBox<String> combo = view.getComboBusqueda();
        combo.removeAllItems();
        combo.addItem("ID Paciente");
        combo.addItem("Nombre Paciente");
    }

    private void cargarRecetas() {
        view.showLoading(true);
        SwingWorker<List<RecetaResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<RecetaResponseDto> doInBackground() throws Exception {
                return recetaService.listRecetasAsync().get();
            }
            @Override
            protected void done() {
                try {
                    List<RecetaResponseDto> recetas = get();
                    view.getTableModel().setRecetas(recetas);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error al cargar recetas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    view.showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void buscarRecetas() {
        String criterio = (String) view.getComboBusqueda().getSelectedItem();
        String filtro = view.getDatoPacienteField().getText().trim().toLowerCase();
        List<RecetaResponseDto> todas = view.getTableModel().getRecetas();

        if (filtro.isEmpty()) {
            cargarRecetas();
            return;
        }

        List<RecetaResponseDto> filtradas = switch (criterio) {
            case "ID Paciente" -> todas.stream()
                    .filter(r -> String.valueOf(r.getIdPaciente()).contains(filtro))
                    .toList();
            case "Nombre Paciente" -> {
                if (pacientes == null) yield List.of();
                var ids = pacientes.stream()
                        .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                        .map(PacienteResponseDto::getId)
                        .toList();
                yield todas.stream()
                        .filter(r -> ids.contains(r.getIdPaciente()))
                        .toList();
            }
            default -> todas;
        };
        view.getTableModel().setRecetas(filtradas);
    }

    private void mostrarDetalles() {
        int row = view.getRecetasTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una receta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        RecetaResponseDto receta = view.getTableModel().getRecetaAt(row);
        PacienteResponseDto paciente = pacientes != null
                ? pacientes.stream().filter(p -> p.getId() == receta.getIdPaciente()).findFirst().orElse(null)
                : null;

        StringBuilder sb = new StringBuilder();
        sb.append("ID Receta: ").append(receta.getId()).append("\n");
        sb.append("Paciente: ").append(paciente != null ? paciente.getNombre() : receta.getIdPaciente()).append("\n");
        sb.append("Médico: ").append(receta.getIdMedico()).append("\n");
        sb.append("Fecha Confección: ").append(receta.getFechaConfeccion()).append("\n");
        sb.append("Fecha Retiro: ").append(receta.getFechaRetiro()).append("\n");
        sb.append("Estado: ").append(receta.getEstado()).append("\n");
        JOptionPane.showMessageDialog(view, sb.toString(), "Detalles de Receta", JOptionPane.INFORMATION_MESSAGE);
    }
}
