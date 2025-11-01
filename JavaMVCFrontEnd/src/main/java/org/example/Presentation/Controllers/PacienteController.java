package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Paciente.*;
import org.example.Presentation.Observable;
import org.example.Presentation.Views.PacienteForm;
import org.example.Services.PacienteService;
import org.example.Utilities.ChangeType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PacienteController extends Observable {
    private final PacienteForm pacienteView;
    private final PacienteService pacienteService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PacienteController(PacienteForm pacienteView, PacienteService pacienteService) {
        this.pacienteView = pacienteView;
        this.pacienteService = pacienteService;

        addObserver(pacienteView.getTableModel());
        loadPacientesAsync();
        addListeners();
    }

    private void loadPacientesAsync() {
        showLoading(true);

        SwingWorker<List<PacienteResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PacienteResponseDto> doInBackground() throws Exception {
                return pacienteService.listPacientesAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<PacienteResponseDto> pacientes = get();
                    pacienteView.getTableModel().setPacientes(pacientes);
                } catch (Exception e) {
                    handleError("Error al cargar pacientes", e);
                } finally {
                    showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        pacienteView.getGuardarButton().addActionListener(e -> handleAddPaciente());
        pacienteView.getActualizarButton().addActionListener(e -> handleUpdatePaciente());
        pacienteView.getBorrarButton().addActionListener(e -> handleDeletePaciente());
        pacienteView.getLimpiarButton().addActionListener(e -> handleClearFields());
        pacienteView.getBuscarButton().addActionListener(e -> handleSearch());
        pacienteView.getReporteButton().addActionListener(e -> handleReport());
        pacienteView.getPatientsTable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    private void handleAddPaciente() {
        if (!validateFields()) return;

        try {
            String nombre = pacienteView.getNameTextField().getText().trim();
            String telefono = pacienteView.getTelefonoTextField().getText().trim();
            Date fechaNacimiento = pacienteView.getDatePicker().getDate();

            String fechaNacStr = fechaNacimiento != null ? dateFormat.format(fechaNacimiento) : null;
            AddPacienteRequestDto dto = new AddPacienteRequestDto(nombre, telefono, fechaNacStr);

            showLoading(true);
            executeAsync(
                    () -> pacienteService.addPacienteAsync(dto).get(),
                    paciente -> {
                        if (paciente != null) {
                            notifyObservers(ChangeType.CREATED, paciente);
                            pacienteView.clearFields();
                            showSuccess("Paciente agregado exitosamente");
                        }
                    },
                    "Error al agregar paciente"
            );
        } catch (Exception ex) {
            handleError("Error en los datos del paciente", ex);
        }
    }

    private void handleUpdatePaciente() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un paciente de la tabla");
            return;
        }

        if (!validateFields()) return;

        try {
            String idText = pacienteView.getIdTextField().getText().trim();
            String nombre = pacienteView.getNameTextField().getText().trim();
            String telefono = pacienteView.getTelefonoTextField().getText().trim();
            Date fechaNacimiento = pacienteView.getDatePicker().getDate();

            int id = Integer.parseInt(idText);
            String fechaNacStr = fechaNacimiento != null ? dateFormat.format(fechaNacimiento) : null;
            UpdatePacienteRequestDto dto = new UpdatePacienteRequestDto(id, nombre, telefono, fechaNacStr);

            showLoading(true);
            executeAsync(
                    () -> pacienteService.updatePacienteAsync(dto).get(),
                    paciente -> {
                        if (paciente != null) {
                            notifyObservers(ChangeType.UPDATED, paciente);
                            pacienteView.clearFields();
                            showSuccess("Paciente actualizado exitosamente");
                        }
                    },
                    "Error al actualizar paciente"
            );
        } catch (NumberFormatException ex) {
            showError("ID debe ser un número válido");
        }
    }

    private void handleDeletePaciente() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un paciente de la tabla");
            return;
        }

        PacienteResponseDto selectedPaciente = pacienteView.getTableModel().getPacienteAt(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                pacienteView,
                "¿Está seguro de eliminar al paciente " + selectedPaciente.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DeletePacienteRequestDto dto = new DeletePacienteRequestDto(selectedPaciente.getId());

            showLoading(true);
            executeAsync(
                    () -> pacienteService.deletePacienteAsync(dto).get(),
                    success -> {
                        if (success) {
                            notifyObservers(ChangeType.DELETED, selectedPaciente.getId());
                            pacienteView.clearFields();
                            showSuccess("Paciente eliminado exitosamente");
                        }
                    },
                    "Error al eliminar paciente"
            );
        }
    }

    private void handleClearFields() {
        pacienteView.clearFields();
    }

    private void handleSearch() {
        String filtro = pacienteView.getBuscarTextField().getText().toLowerCase().trim();

        if (filtro.isEmpty()) {
            loadPacientesAsync();
            return;
        }

        List<PacienteResponseDto> allPacientes = pacienteView.getTableModel().getPacientes();
        List<PacienteResponseDto> filtrados = allPacientes.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro) ||
                        (p.getTelefono() != null && p.getTelefono().contains(filtro)) ||
                        String.valueOf(p.getId()).contains(filtro))
                .toList();

        pacienteView.getTableModel().setPacientes(filtrados);
    }

    private void handleReport() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un paciente de la tabla");
            return;
        }

        PacienteResponseDto paciente = pacienteView.getTableModel().getPacienteAt(selectedRow);
        String reporte = String.format(
                "=== REPORTE DE PACIENTE ===\n\n" +
                        "ID: %d\n" +
                        "Nombre: %s\n" +
                        "Teléfono: %s\n" +
                        "Fecha Nacimiento: %s\n",
                paciente.getId(),
                paciente.getNombre(),
                paciente.getTelefono() != null ? paciente.getTelefono() : "N/A",
                paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento() : "N/A"
        );

        JOptionPane.showMessageDialog(
                pacienteView,
                reporte,
                "Reporte de Paciente",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = pacienteView.getPatientsTable().getSelectedRow();
            if (row >= 0) {
                PacienteResponseDto paciente = pacienteView.getTableModel().getPacienteAt(row);
                pacienteView.populateFields(paciente);
            }
        }
    }

    // ---------------------------
    // Validation & Utility Methods
    // ---------------------------
    private boolean validateFields() {
        String nombre = pacienteView.getNameTextField().getText().trim();

        if (nombre.isEmpty()) {
            showWarning("El nombre es requerido");
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
        pacienteView.showLoading(visible);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(pacienteView, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(pacienteView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(pacienteView, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void handleError(String message, Exception ex) {
        ex.printStackTrace();
        showError(message + ": " + ex.getMessage());
    }

    @FunctionalInterface
    private interface AsyncTask<T> {
        T execute() throws Exception;
    }

    @FunctionalInterface
    private interface ResultHandler<T> {
        void handle(T result);
    }
}