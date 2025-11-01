// java
package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.RecetaTableModel;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Services.RecetaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistoricoRecetasView extends JPanel {

    private JPanel mainPanel;
    private JPanel searchPanel;
    private JComboBox<String> comboBusqueda;
    private JTextField DatoPacienteField;
    private JButton buscarButton;
    private JButton DetallesButton;
    private JPanel infoPanel;
    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;
    private JPanel tablePanel;
    private JScrollPane scrollPane;
    private JTable recetasTable;
    private final LoadingOverlay overlay;

    private final RecetaService recetaService;

    public HistoricoRecetasView(JFrame parent, RecetaService recetaService) {
        this.recetaService = recetaService;
        this.overlay = new LoadingOverlay(parent);

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(DatoPacienteField, BorderLayout.CENTER);
        top.add(buscarButton, BorderLayout.EAST);

        recetasTable.setModel(recetasTable.getModel());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(recetasTable), BorderLayout.CENTER);

        buscarButton.addActionListener(e -> loadRecetasAsync());
        loadRecetasAsync();
    }

    private void loadRecetasAsync() {
        String filtro = DatoPacienteField.getText().trim().toLowerCase();

        overlay.show(true);
        SwingWorker<List<RecetaResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<RecetaResponseDto> doInBackground() throws Exception {
                return recetaService.listRecetasAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<RecetaResponseDto> recetas = get();
                    if (filtro.isEmpty()) {
                        RecetaTableModel tableModel = (RecetaTableModel) recetasTable.getModel();
                        tableModel.setRecetas(recetas);
                    } else {
                        var filtradas = recetas.stream()
                                .filter(r -> String.valueOf(r.getId()).contains(filtro)
                                        || String.valueOf(r.getIdPaciente()).contains(filtro)
                                        || String.valueOf(r.getIdMedico()).contains(filtro)
                                        || (r.getEstado() != null && r.getEstado().toLowerCase().contains(filtro)))
                                .toList();
                        RecetaTableModel tableModel = (RecetaTableModel) recetasTable.getModel();
                        tableModel.setRecetas(filtradas);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HistoricoRecetasView.this,
                            "Error al cargar recetas: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    overlay.show(false);
                }
            }
        };
        worker.execute();
    }

    public void showLoading(boolean visible) {
        overlay.show(visible);
    }

    private void createUIComponents() {
        DetallesButton = new BlueRoundedButton("Historico Recetas");
        buscarButton = new BlueRoundedButton("Historico Recetas");
    }
}
