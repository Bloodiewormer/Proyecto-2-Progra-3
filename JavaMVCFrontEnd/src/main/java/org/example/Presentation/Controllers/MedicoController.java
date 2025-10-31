package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Medico.*;
import org.example.Presentation.Observable;
import org.example.Presentation.Views.MedicoForm;
import org.example.Services.UsuarioService;
import org.example.Utilities.ChangeType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class MedicoController extends Observable {
    private final MedicoForm medicoView;
    private final UsuarioService usuarioService;

    public MedicoController(MedicoForm medicoView, UsuarioService usuarioService) {
        this.medicoView = medicoView;
        this.usuarioService = usuarioService;

        addObserver(medicoView.getTableModel());
        loadMedicosAsync();
        addListeners();
    }

    private void loadMedicosAsync() {
        showLoading(true);

        SwingWorker<List<MedicoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MedicoResponseDto> doInBackground() throws Exception {
                return usuarioService.listMedicosAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<MedicoResponseDto> medicos = get();
                    medicoView.getTableModel().setMedicos(medicos);
                } catch (Exception e) {
                    handleError("Error al cargar médicos", e);
                } finally {
                    showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        medicoView.getGuardarButton().addActionListener(e -> handleAddMedico());
        medicoView.getActualizarButton().addActionListener(e -> handleUpdateMedico());
        medicoView.getBorrarButton().addActionListener(e -> handleDeleteMedico());
        medicoView.getLimpiarButton().addActionListener(e -> handleClearFields());
        medicoView.getBuscarButton().addActionListener(e -> handleSearch());
        medicoView.getReporteButton().addActionListener(e -> handleReport());
        medicoView.getMedicostable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    // ---------------------------
    // Action Handlers
    // ---------------------------
    private void handleAddMedico() {
        if (!validateFields()) return;

        try {
            String nombre = medicoView.getNametextField().getText().trim();
            String especialidad = medicoView.getEspecialidadtextField().getText().trim();

            AddMedicoRequestDto dto = new AddMedicoRequestDto(nombre, "default123", especialidad);

            showLoading(true);
            executeAsync(
                    () -> usuarioService.addMedicoAsync(dto).get(),
                    medico -> {
                        if (medico != null) {
                            notifyObservers(ChangeType.CREATED, medico);
                            medicoView.clearFields();
                            showSuccess("Médico agregado exitosamente");
                        }
                    },
                    "Error al agregar médico"
            );
        } catch (Exception ex) {
            handleError("Error en los datos del médico", ex);
        }
    }

    private void handleUpdateMedico() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un médico de la tabla");
            return;
        }

        if (!validateFields()) return;

        try {
            String idText = medicoView.getIDtextField().getText().trim();
            String nombre = medicoView.getNametextField().getText().trim();
            String especialidad = medicoView.getEspecialidadtextField().getText().trim();

            int id = Integer.parseInt(idText);
            UpdateMedicoRequestDto dto = new UpdateMedicoRequestDto(id, nombre, null, especialidad);

            showLoading(true);
            executeAsync(
                    () -> usuarioService.updateMedicoAsync(dto).get(),
                    medico -> {
                        if (medico != null) {
                            notifyObservers(ChangeType.UPDATED, medico);
                            medicoView.clearFields();
                            showSuccess("Médico actualizado exitosamente");
                        }
                    },
                    "Error al actualizar médico"
            );
        } catch (NumberFormatException ex) {
            showError("ID debe ser un número válido");
        }
    }

    private void handleDeleteMedico() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un médico de la tabla");
            return;
        }

        MedicoResponseDto selectedMedico = medicoView.getTableModel().getMedicoAt(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                medicoView,
                "¿Está seguro de eliminar al médico " + selectedMedico.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DeleteMedicoRequestDto dto = new DeleteMedicoRequestDto(selectedMedico.getId());

            showLoading(true);
            executeAsync(
                    () -> usuarioService.deleteMedicoAsync(dto).get(),
                    success -> {
                        if (success) {
                            notifyObservers(ChangeType.DELETED, selectedMedico.getId());
                            medicoView.clearFields();
                            showSuccess("Médico eliminado exitosamente");
                        }
                    },
                    "Error al eliminar médico"
            );
        }
    }

    private void handleClearFields() {
        medicoView.clearFields();
    }

    private void handleSearch() {
        String filtro = medicoView.getBuscartextField().getText().toLowerCase().trim();

        if (filtro.isEmpty()) {
            loadMedicosAsync();
            return;
        }

        List<MedicoResponseDto> allMedicos = medicoView.getTableModel().getMedicos();
        List<MedicoResponseDto> filtrados = allMedicos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(filtro) ||
                        m.getEspecialidad().toLowerCase().contains(filtro) ||
                        String.valueOf(m.getId()).contains(filtro))
                .toList();

        medicoView.getTableModel().setMedicos(filtrados);
    }

    private void handleReport() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un médico de la tabla");
            return;
        }

        MedicoResponseDto medico = medicoView.getTableModel().getMedicoAt(selectedRow);
        String reporte = String.format(
                "=== REPORTE DE MÉDICO ===\n\n" +
                        "ID: %d\n" +
                        "Nombre: %s\n" +
                        "Especialidad: %s\n",
                medico.getId(),
                medico.getNombre(),
                medico.getEspecialidad()
        );

        JOptionPane.showMessageDialog(
                medicoView,
                reporte,
                "Reporte de Médico",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = medicoView.getMedicostable().getSelectedRow();
            if (row >= 0) {
                MedicoResponseDto medico = medicoView.getTableModel().getMedicoAt(row);
                medicoView.populateFields(medico);
            }
        }
    }

    // ---------------------------
    // Validation & Utility Methods
    // ---------------------------
    private boolean validateFields() {
        String nombre = medicoView.getNametextField().getText().trim();
        String especialidad = medicoView.getEspecialidadtextField().getText().trim();

        if (nombre.isEmpty()) {
            showWarning("El nombre es requerido");
            return false;
        }

        if (especialidad.isEmpty()) {
            showWarning("La especialidad es requerida");
            return false;
        }

        return true;
    }

    private <T> void executeAsync(AsyncTask<T> task, ResultHandler<T> onSuccess, String errorMessage) {
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.execute();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    onSuccess.handle(result);
                } catch (Exception ex) {
                    handleError(errorMessage, ex);
                } finally {
                    showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void showLoading(boolean visible) {
        medicoView.showLoading(visible);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(medicoView, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(medicoView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(medicoView, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void handleError(String message, Exception ex) {
        ex.printStackTrace();
        showError(message + ": " + ex.getMessage());
    }

    // Functional interfaces for cleaner async code
    @FunctionalInterface
    private interface AsyncTask<T> {
        T execute() throws Exception;
    }

    @FunctionalInterface
    private interface ResultHandler<T> {
        void handle(T result);
    }
}