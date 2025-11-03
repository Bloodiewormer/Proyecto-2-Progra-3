package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Presentation.Views.DespachoForm;
import org.example.Services.DespachoService;
import org.example.Utilities.EstadoReceta;

import javax.swing.*;
import java.util.List;

public class DespachoController {
    private final DespachoForm view;
    private final DespachoService service;
    private final int idFarmaceuta;

    public DespachoController(DespachoForm view, DespachoService service, int idFarmaceuta) {
        this.view = view;
        this.service = service;
        this.idFarmaceuta = idFarmaceuta;
    }

    public void cargarRecetas() {
        new SwingWorker<List<RecetaResponseDto>, Void>() {
            @Override
            protected List<RecetaResponseDto> doInBackground() {
                return service.listarRecetas();
            }

            @Override
            protected void done() {
                try {
                    List<RecetaResponseDto> recetas = get();
                    view.getRecetaTableModel().setRecetas(recetas);
                } catch (Exception e) {
                    view.mostrarError("Error cargando recetas: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void buscarPaciente() {
        String idTexto = view.getDatoPacienteField().getText().trim();

        if (idTexto.isEmpty()) {
            view.mostrarError("Debe ingresar un ID de paciente");
            return;
        }

        try {
            int idPaciente = Integer.parseInt(idTexto);

            new SwingWorker<PacienteResponseDto, Void>() {
                @Override
                protected PacienteResponseDto doInBackground() {
                    return service.buscarPaciente(idPaciente);
                }

                @Override
                protected void done() {
                    try {
                        PacienteResponseDto paciente = get();
                        if (paciente != null) {
                            view.mostrarPaciente(paciente);
                            cargarRecetasPaciente(idPaciente);
                        } else {
                            view.mostrarError("Paciente no encontrado");
                        }
                    } catch (Exception e) {
                        view.mostrarError("Error buscando paciente: " + e.getMessage());
                    }
                }
            }.execute();

        } catch (NumberFormatException e) {
            view.mostrarError("ID de paciente debe ser un número");
        }
    }

    private void cargarRecetasPaciente(int idPaciente) {
        new SwingWorker<List<RecetaResponseDto>, Void>() {
            @Override
            protected List<RecetaResponseDto> doInBackground() {
                return service.listarPorPaciente(idPaciente);
            }

            @Override
            protected void done() {
                try {
                    List<RecetaResponseDto> recetas = get();
                    view.getRecetaTableModel().setRecetas(recetas);

                    if (recetas.isEmpty()) {
                        view.mostrarMensaje("No hay recetas disponibles para este paciente");
                    }
                } catch (Exception e) {
                    view.mostrarError("Error cargando recetas del paciente: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void cambiarEstado() {
        Integer recetaId = view.getRecetaSeleccionadaId();

        if (recetaId == null) {
            view.mostrarError("Debe seleccionar una receta");
            return;
        }

        EstadoReceta nuevoEstado = (EstadoReceta) view.getEstadoComboBox().getSelectedItem();
        if (nuevoEstado == null) {
            view.mostrarError("Debe seleccionar un estado");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de cambiar el estado de la receta a " + nuevoEstado + "?",
                "Confirmar cambio de estado",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        new SwingWorker<RecetaResponseDto, Void>() {
            @Override
            protected RecetaResponseDto doInBackground() {
                return service.actualizarEstado(recetaId, nuevoEstado.name());
            }

            @Override
            protected void done() {
                try {
                    RecetaResponseDto receta = get();
                    if (receta != null) {
                        view.mostrarMensaje("Estado actualizado exitosamente");

                        if (view.getPacienteSeleccionadoId() != null) {
                            cargarRecetasPaciente(view.getPacienteSeleccionadoId());
                        } else {
                            cargarRecetas();
                        }
                    } else {
                        view.mostrarError("Error actualizando estado");
                    }
                } catch (Exception e) {
                    view.mostrarError("Error actualizando estado: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void limpiar() {
        view.limpiarPaciente();
        view.getDatoPacienteField().setText("");
        cargarRecetas();
    }
}