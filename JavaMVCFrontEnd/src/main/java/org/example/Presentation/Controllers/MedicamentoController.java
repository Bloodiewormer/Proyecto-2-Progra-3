package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Medicamento.*;
import org.example.Presentation.Observable;
import org.example.Presentation.Models.MedicamentoTableModel;
import org.example.Presentation.Views.MedicamentoForm;
import org.example.Services.MedicamentoService;
import org.example.Utilities.ChangeType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class MedicamentoController extends Observable {
    private final MedicamentoForm medicamentoView;
    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoForm medicamentoView, MedicamentoService medicamentoService) {
        this.medicamentoView = medicamentoView;
        this.medicamentoService = medicamentoService;

        addObserver(medicamentoView.getTableModel());
        loadMedicamentosAsync();
        addListeners();
    }

    private void loadMedicamentosAsync() {
        medicamentoView.showLoading(true);

        SwingWorker<List<MedicamentoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MedicamentoResponseDto> doInBackground() throws Exception {
                return medicamentoService.listMedicamentosAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<MedicamentoResponseDto> medicamentos = get();
                    if (medicamentos != null) {
                        medicamentoView.getTableModel().setMedicamentos(medicamentos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error al cargar medicamentos: " + e.getMessage());
                } finally {
                    medicamentoView.showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        medicamentoView.getGuardarButton().addActionListener(e -> handleAddMedicamento());
        medicamentoView.getActualizarButton().addActionListener(e -> handleUpdateMedicamento());
        medicamentoView.getBorrarButton().addActionListener(e -> handleDeleteMedicamento());
        medicamentoView.getLimpiarButton().addActionListener(e -> handleClearFields());
        medicamentoView.getBuscarButton().addActionListener(e -> handleSearch());
        medicamentoView.getReporteButton().addActionListener(e -> handleReport());
        medicamentoView.getMedicamentotable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    private void handleAddMedicamento() {
        if (!validateFields()) return;

        try {
            String nombre = medicamentoView.getNametextField().getText().trim();
            String presentacion = medicamentoView.getPresentaciontextField().getText().trim();

            AddMedicamentoRequestDto dto = new AddMedicamentoRequestDto(nombre, presentacion);

            medicamentoView.showLoading(true);
            SwingWorker<MedicamentoResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected MedicamentoResponseDto doInBackground() throws Exception {
                    return medicamentoService.addMedicamentoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        MedicamentoResponseDto medicamento = get();
                        if (medicamento != null) {
                            notifyObservers(ChangeType.CREATED, medicamento);
                            medicamentoView.clearFields();
                            showSuccess("Medicamento agregado exitosamente");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Error al agregar: " + ex.getMessage());
                    } finally {
                        medicamentoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void handleUpdateMedicamento() {
        int selectedRow = medicamentoView.getMedicamentotable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un medicamento de la tabla");
            return;
        }

        if (!validateFields()) return;

        try {
            String codigoText = medicamentoView.getCodigotextField().getText().trim();
            String nombre = medicamentoView.getNametextField().getText().trim();
            String presentacion = medicamentoView.getPresentaciontextField().getText().trim();

            int codigo = Integer.parseInt(codigoText);
            UpdateMedicamentoRequestDto dto = new UpdateMedicamentoRequestDto(codigo, nombre, presentacion);

            medicamentoView.showLoading(true);
            SwingWorker<MedicamentoResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected MedicamentoResponseDto doInBackground() throws Exception {
                    return medicamentoService.updateMedicamentoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        MedicamentoResponseDto updatedMedicamento = get();
                        if (updatedMedicamento != null) {
                            notifyObservers(ChangeType.UPDATED, updatedMedicamento);
                            medicamentoView.clearFields();
                            showSuccess("Medicamento actualizado exitosamente");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Error al actualizar: " + ex.getMessage());
                    } finally {
                        medicamentoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            showError("Código debe ser un número válido");
        }
    }

    private void handleDeleteMedicamento() {
        int selectedRow = medicamentoView.getMedicamentotable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un medicamento de la tabla");
            return;
        }

        MedicamentoResponseDto selectedMedicamento = medicamentoView.getTableModel().getMedicamentoAt(selectedRow);
        if (selectedMedicamento == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                medicamentoView,
                "¿Está seguro de eliminar el medicamento " + selectedMedicamento.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DeleteMedicamentoRequestDto dto = new DeleteMedicamentoRequestDto(selectedMedicamento.getId());

            medicamentoView.showLoading(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return medicamentoService.deleteMedicamentoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (Boolean.TRUE.equals(success)) {
                            notifyObservers(ChangeType.DELETED, selectedMedicamento.getId());
                            medicamentoView.clearFields();
                            showSuccess("Medicamento eliminado exitosamente");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Error al eliminar: " + ex.getMessage());
                    } finally {
                        medicamentoView.showLoading(false);
                    }
                }
            };
            worker.execute();
        }
    }

    private void handleClearFields() {
        medicamentoView.clearFields();
    }

    private void handleSearch() {
        String filtro = medicamentoView.getBuscartextField().getText().toLowerCase().trim();

        if (filtro.isEmpty()) {
            loadMedicamentosAsync();
            return;
        }

        List<MedicamentoResponseDto> allMedicamentos = medicamentoView.getTableModel().getMedicamentos();
        List<MedicamentoResponseDto> filtrados = allMedicamentos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(filtro) ||
                        m.getPresentacion().toLowerCase().contains(filtro) ||
                        String.valueOf(m.getId()).contains(filtro))
                .toList();

        medicamentoView.getTableModel().setMedicamentos(filtrados);
    }

    private void handleReport() {
        int selectedRow = medicamentoView.getMedicamentotable().getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un medicamento de la tabla");
            return;
        }

        MedicamentoResponseDto medicamento = medicamentoView.getTableModel().getMedicamentoAt(selectedRow);
        if (medicamento == null) return;

        String reporte = String.format(
                "Reporte de Medicamento\n\n" +
                        "Código: %d\n" +
                        "Nombre: %s\n" +
                        "Presentación: %s\n",
                medicamento.getId(),
                medicamento.getNombre(),
                medicamento.getPresentacion()
        );

        JOptionPane.showMessageDialog(
                medicamentoView,
                reporte,
                "Reporte de Medicamento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = medicamentoView.getMedicamentotable().getSelectedRow();
            if (row >= 0) {
                MedicamentoResponseDto medicamento = medicamentoView.getTableModel().getMedicamentoAt(row);
                if (medicamento != null) {
                    medicamentoView.populateFields(medicamento);
                }
            }
        }
    }

    private boolean validateFields() {
        String nombre = medicamentoView.getNametextField().getText().trim();
        String presentacion = medicamentoView.getPresentaciontextField().getText().trim();

        if (nombre.isEmpty() || presentacion.isEmpty()) {
            showWarning("Nombre y Presentación son requeridos");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(medicamentoView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(medicamentoView, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(medicamentoView, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}