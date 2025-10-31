package org.example.Presentation.Controllers;

import org.example.Presentation.Observable;
import org.example.Presentation.Views.MedicamentoForm;
import org.example.Services.MedicamentoService;
import org.example.Utilities.EventType;

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
                    medicamentoView.getTableModel().setMedicamentos(medicamentos);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            medicamentoView,
                            "Error al cargar medicamentos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
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

    // ---------------------------
    // Action Handlers
    // ---------------------------
    private void handleAddMedicamento() {
        try {
            String codigoText = medicamentoView.getCodigotextField().getText().trim();
            String nombre = medicamentoView.getNametextField().getText().trim();
            String presentacion = medicamentoView.getPresentaciontextField().getText().trim();

            if (codigoText.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
                JOptionPane.showMessageDialog(
                        medicamentoView,
                        "Todos los campos son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int codigo = Integer.parseInt(codigoText);
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
                            notifyObservers(EventType.CREATED, medicamento);
                            medicamentoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicamentoView,
                                    "Medicamento agregado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicamentoView,
                                "Error al agregar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        medicamentoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    medicamentoView,
                    "Código debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleUpdateMedicamento() {
        int selectedRow = medicamentoView.getMedicamentotable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    medicamentoView,
                    "Seleccione un medicamento de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String codigoText = medicamentoView.getCodigotextField().getText().trim();
            String nombre = medicamentoView.getNametextField().getText().trim();
            String presentacion = medicamentoView.getPresentaciontextField().getText().trim();

            if (codigoText.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
                JOptionPane.showMessageDialog(
                        medicamentoView,
                        "Todos los campos son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

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
                            notifyObservers(EventType.UPDATED, updatedMedicamento);
                            medicamentoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicamentoView,
                                    "Medicamento actualizado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicamentoView,
                                "Error al actualizar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        medicamentoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    medicamentoView,
                    "Código debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDeleteMedicamento() {
        int selectedRow = medicamentoView.getMedicamentotable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    medicamentoView,
                    "Seleccione un medicamento de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MedicamentoResponseDto selectedMedicamento = medicamentoView.getTableModel().getMedicamentoAt(selectedRow);

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
                        if (success) {
                            notifyObservers(EventType.DELETED, selectedMedicamento.getId());
                            medicamentoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicamentoView,
                                    "Medicamento eliminado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicamentoView,
                                "Error al eliminar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
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
            JOptionPane.showMessageDialog(
                    medicamentoView,
                    "Seleccione un medicamento de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MedicamentoResponseDto medicamento = medicamentoView.getTableModel().getMedicamentoAt(selectedRow);
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
                medicamentoView.populateFields(medicamento);
            }
        }
    }
}