package org.example.Presentation.Controllers;

import org.example.Presentation.Observable;
import org.example.Presentation.Views.PacienteForm;
import org.example.Services.PacienteService;
import org.example.Utilities.EventType;

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
        pacienteView.showLoading(true);

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
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            pacienteView,
                            "Error al cargar pacientes: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    pacienteView.showLoading(false);
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
        try {
            String idText = pacienteView.getIdTextField().getText().trim();
            String nombre = pacienteView.getNameTextField().getText().trim();
            String telefono = pacienteView.getTelefonoTextField().getText().trim();
            Date fechaNacimiento = pacienteView.getDatePicker().getDate();

            if (idText.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        pacienteView,
                        "ID y Nombre son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String fechaNacStr = fechaNacimiento != null ? dateFormat.format(fechaNacimiento) : null;
            AddPacienteRequestDto dto = new AddPacienteRequestDto(nombre, telefono, fechaNacStr);

            pacienteView.showLoading(true);
            SwingWorker<PacienteResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected PacienteResponseDto doInBackground() throws Exception {
                    return pacienteService.addPacienteAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        PacienteResponseDto paciente = get();
                        if (paciente != null) {
                            notifyObservers(EventType.CREATED, paciente);
                            pacienteView.clearFields();
                            JOptionPane.showMessageDialog(
                                    pacienteView,
                                    "Paciente agregado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                pacienteView,
                                "Error al agregar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        pacienteView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    pacienteView,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleUpdatePaciente() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    pacienteView,
                    "Seleccione un paciente de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String idText = pacienteView.getIdTextField().getText().trim();
            String nombre = pacienteView.getNameTextField().getText().trim();
            String telefono = pacienteView.getTelefonoTextField().getText().trim();
            Date fechaNacimiento = pacienteView.getDatePicker().getDate();

            if (idText.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        pacienteView,
                        "ID y Nombre son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int id = Integer.parseInt(idText);
            String fechaNacStr = fechaNacimiento != null ? dateFormat.format(fechaNacimiento) : null;
            UpdatePacienteRequestDto dto = new UpdatePacienteRequestDto(id, nombre, telefono, fechaNacStr);

            pacienteView.showLoading(true);
            SwingWorker<PacienteResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected PacienteResponseDto doInBackground() throws Exception {
                    return pacienteService.updatePacienteAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        PacienteResponseDto updatedPaciente = get();
                        if (updatedPaciente != null) {
                            notifyObservers(EventType.UPDATED, updatedPaciente);
                            pacienteView.clearFields();
                            JOptionPane.showMessageDialog(
                                    pacienteView,
                                    "Paciente actualizado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                pacienteView,
                                "Error al actualizar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        pacienteView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    pacienteView,
                    "ID debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDeletePaciente() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    pacienteView,
                    "Seleccione un paciente de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
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

            pacienteView.showLoading(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return pacienteService.deletePacienteAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success) {
                            notifyObservers(EventType.DELETED, selectedPaciente.getId());
                            pacienteView.clearFields();
                            JOptionPane.showMessageDialog(
                                    pacienteView,
                                    "Paciente eliminado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                pacienteView,
                                "Error al eliminar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        pacienteView.showLoading(false);
                    }
                }
            };
            worker.execute();
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
                .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                .toList();

        pacienteView.getTableModel().setPacientes(filtrados);
    }

    private void handleReport() {
        int selectedRow = pacienteView.getPatientsTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    pacienteView,
                    "Seleccione un paciente de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        PacienteResponseDto paciente = pacienteView.getTableModel().getPacienteAt(selectedRow);
        String reporte = String.format(
                "Reporte de Paciente\n\n" +
                        "ID: %d\n" +
                        "Nombre: %s\n" +
                        "Teléfono: %s\n" +
                        "Fecha Nacimiento: %s\n",
                paciente.getId(),
                paciente.getNombre(),
                paciente.getTelefono(),
                paciente.getFechaNacimiento()
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
}