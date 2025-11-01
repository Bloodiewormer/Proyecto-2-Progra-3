package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Farmaceuta.AddFarmaceutaRequestDto;
import org.example.Domain.Dtos.Farmaceuta.DeleteFarmaceutaRequestDto;
import org.example.Domain.Dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Domain.Dtos.Farmaceuta.UpdateFarmaceutaRequestDto;
import org.example.Presentation.Observable;
import org.example.Presentation.Views.FarmaceutaForm;
import org.example.Services.UsuarioService;
import org.example.Utilities.ChangeType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class FarmaceutaController extends Observable {
    private final FarmaceutaForm farmaceutaView;
    private final UsuarioService usuarioService;

    public FarmaceutaController(FarmaceutaForm farmaceutaView, UsuarioService usuarioService) {
        this.farmaceutaView = farmaceutaView;
        this.usuarioService = usuarioService;

        addObserver(farmaceutaView.getTableModel());
        loadFarmaceutasAsync();
        addListeners();
    }

    private void loadFarmaceutasAsync() {
        farmaceutaView.showLoading(true);

        SwingWorker<List<FarmaceutaResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<FarmaceutaResponseDto> doInBackground() throws Exception {
                return usuarioService.listFarmaceutasAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<FarmaceutaResponseDto> farmaceutas = get();
                    farmaceutaView.getTableModel().setFarmaceutas(farmaceutas);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            farmaceutaView,
                            "Error al cargar farmaceutas: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    farmaceutaView.showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        farmaceutaView.getGuardarButton().addActionListener(e -> handleAddFarmaceuta());
        farmaceutaView.getActualizarButton().addActionListener(e -> handleUpdateFarmaceuta());
        farmaceutaView.getBorrarButton().addActionListener(e -> handleDeleteFarmaceuta());
        farmaceutaView.getLimpiarButton().addActionListener(e -> handleClearFields());
        farmaceutaView.getBuscarButton().addActionListener(e -> handleSearch());
        farmaceutaView.getReporteButton().addActionListener(e -> handleReport());
        farmaceutaView.getFarmaceutatable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    // ---------------------------
    // Action Handlers
    // ---------------------------
    private void handleAddFarmaceuta() {
        try {
            String idText = farmaceutaView.getIDtextFiel().getText().trim();
            String nombre = farmaceutaView.getNametextField().getText().trim();

            if (idText.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        farmaceutaView,
                        "Todos los campos son requeridos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int id = Integer.parseInt(idText);
            AddFarmaceutaRequestDto dto = new AddFarmaceutaRequestDto(id, nombre, "default123");

            farmaceutaView.showLoading(true);
            SwingWorker<FarmaceutaResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected FarmaceutaResponseDto doInBackground() throws Exception {
                    return usuarioService.addFarmaceutaAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        FarmaceutaResponseDto farmaceuta = get();
                        if (farmaceuta != null) {
                            notifyObservers(ChangeType.CREATED, farmaceuta);
                            farmaceutaView.clearFields();
                            JOptionPane.showMessageDialog(
                                    farmaceutaView,
                                    "Farmaceuta agregado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                farmaceutaView,
                                "Error al agregar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        farmaceutaView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    farmaceutaView,
                    "ID debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleUpdateFarmaceuta() {
        int selectedRow = farmaceutaView.getFarmaceutatable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    farmaceutaView,
                    "Seleccione un farmaceuta de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            FarmaceutaResponseDto selectedFarmaceuta = farmaceutaView.getTableModel().getFarmaceutaAt(selectedRow);
            String nombre = farmaceutaView.getNametextField().getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        farmaceutaView,
                        "El nombre es requerido",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            UpdateFarmaceutaRequestDto dto = new UpdateFarmaceutaRequestDto(
                    selectedFarmaceuta.getId(),
                    nombre,
                    null // password no se actualiza desde aquí
            );

            farmaceutaView.showLoading(true);
            SwingWorker<FarmaceutaResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected FarmaceutaResponseDto doInBackground() throws Exception {
                    return usuarioService.updateFarmaceutaAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        FarmaceutaResponseDto updatedFarmaceuta = get();
                        if (updatedFarmaceuta != null) {
                            notifyObservers(ChangeType.UPDATED, updatedFarmaceuta);
                            farmaceutaView.clearFields();
                            JOptionPane.showMessageDialog(
                                    farmaceutaView,
                                    "Farmaceuta actualizado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                farmaceutaView,
                                "Error al actualizar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        farmaceutaView.showLoading(false);
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    farmaceutaView,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDeleteFarmaceuta() {
        int selectedRow = farmaceutaView.getFarmaceutatable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    farmaceutaView,
                    "Seleccione un farmaceuta de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        FarmaceutaResponseDto selectedFarmaceuta = farmaceutaView.getTableModel().getFarmaceutaAt(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                farmaceutaView,
                "¿Está seguro de eliminar al farmaceuta " + selectedFarmaceuta.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DeleteFarmaceutaRequestDto dto = new DeleteFarmaceutaRequestDto(selectedFarmaceuta.getId());

            farmaceutaView.showLoading(true);
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return usuarioService.deleteFarmaceutaAsync(dto).get();
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success) {
                            notifyObservers(ChangeType.DELETED, selectedFarmaceuta.getId());
                            farmaceutaView.clearFields();
                            JOptionPane.showMessageDialog(
                                    farmaceutaView,
                                    "Farmaceuta eliminado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                farmaceutaView,
                                "Error al eliminar: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        farmaceutaView.showLoading(false);
                    }
                }
            };
            worker.execute();
        }
    }

    private void handleClearFields() {
        farmaceutaView.clearFields();
    }

    private void handleSearch() {
        String filtro = farmaceutaView.getBuscartextField().getText().toLowerCase().trim();

        if (filtro.isEmpty()) {
            loadFarmaceutasAsync();
            return;
        }

        // Por ahora filtramos localmente, pero podrías hacer una búsqueda en el servidor
        List<FarmaceutaResponseDto> allFarmaceutas = farmaceutaView.getTableModel().getFarmaceutas();
        List<FarmaceutaResponseDto> filtrados = allFarmaceutas.stream()
                .filter(f -> f.getNombre().toLowerCase().contains(filtro))
                .toList();

        farmaceutaView.getTableModel().setFarmaceutas(filtrados);
    }

    private void handleReport() {
        int selectedRow = farmaceutaView.getFarmaceutatable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    farmaceutaView,
                    "Seleccione un farmaceuta de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        FarmaceutaResponseDto farmaceuta = farmaceutaView.getTableModel().getFarmaceutaAt(selectedRow);
        String reporte = String.format(
                "Reporte de Farmaceuta\n\n" +
                        "ID: %d\n" +
                        "Nombre: %s\n",
                farmaceuta.getId(),
                farmaceuta.getNombre()
        );

        JOptionPane.showMessageDialog(
                farmaceutaView,
                reporte,
                "Reporte de Farmaceuta",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = farmaceutaView.getFarmaceutatable().getSelectedRow();
            if (row >= 0) {
                FarmaceutaResponseDto farmaceuta = farmaceutaView.getTableModel().getFarmaceutaAt(row);
                farmaceutaView.populateFields(farmaceuta);
            }
        }
    }
}