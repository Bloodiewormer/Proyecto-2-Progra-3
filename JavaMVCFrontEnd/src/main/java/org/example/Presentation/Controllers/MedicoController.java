package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Medico.*;
import org.example.Presentation.Observable;
import org.example.Presentation.Models.MedicoTableModel;
import org.example.Presentation.Views.MedicoForm;
import org.example.Services.UsuarioService;
import org.example.Utilities.EventType;

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
        medicoView.showLoading(true);

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
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            medicoView,
                            "Error al cargar médicos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    medicoView.showLoading(false);
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
        try {
            String idText = medicoView.getIDtextField().getText().trim();
            String nombre = medicoView.getNametextField().getText().trim();
            String especialidad = medicoView.getEspecialidadtextField().getText().trim();

            if (idText.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
                JOptionPane.showMessageDialog(
                        medicoView,
                        "Todos los campos son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int id = Integer.parseInt(idText);
            AddMedicoRequestDto dto = new AddMedicoRequestDto(nombre, "default123", especialidad);

            medicoView.showLoading(true);
            SwingWorker<MedicoResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected MedicoResponseDto doInBackground() throws Exception {
                    return usuarioService.addMedicoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        MedicoResponseDto medico = get();
                        if (medico != null) {
                            notifyObservers(EventType.CREATED, medico);
                            medicoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicoView,
                                    "Médico agregado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicoView,
                                "Error al agregar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        medicoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    medicoView,
                    "ID debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleUpdateMedico() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    medicoView,
                    "Seleccione un médico de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String idText = medicoView.getIDtextField().getText().trim();
            String nombre = medicoView.getNametextField().getText().trim();
            String especialidad = medicoView.getEspecialidadtextField().getText().trim();

            if (idText.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
                JOptionPane.showMessageDialog(
                        medicoView,
                        "Todos los campos son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int id = Integer.parseInt(idText);
            UpdateMedicoRequestDto dto = new UpdateMedicoRequestDto(id, nombre, null, especialidad);

            medicoView.showLoading(true);
            SwingWorker<MedicoResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected MedicoResponseDto doInBackground() throws Exception {
                    return usuarioService.updateMedicoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        MedicoResponseDto updatedMedico = get();
                        if (updatedMedico != null) {
                            notifyObservers(EventType.UPDATED, updatedMedico);
                            medicoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicoView,
                                    "Médico actualizado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicoView,
                                "Error al actualizar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        medicoView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    medicoView,
                    "ID debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDeleteMedico() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    medicoView,
                    "Seleccione un médico de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
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

            medicoView.showLoading(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return usuarioService.deleteMedicoAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success) {
                            notifyObservers(EventType.DELETED, selectedMedico.getId());
                            medicoView.clearFields();
                            JOptionPane.showMessageDialog(
                                    medicoView,
                                    "Médico eliminado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                medicoView,
                                "Error al eliminar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        medicoView.showLoading(false);
                    }
                }
            };
            worker.execute();
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
                .filter(m -> m.getNombre().toLowerCase().contains(filtro))
                .toList();

        medicoView.getTableModel().setMedicos(filtrados);
    }

    private void handleReport() {
        int selectedRow = medicoView.getMedicostable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    medicoView,
                    "Seleccione un médico de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MedicoResponseDto medico = medicoView.getTableModel().getMedicoAt(selectedRow);
        String reporte = String.format(
                "Reporte de Médico\n\n" +
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
}