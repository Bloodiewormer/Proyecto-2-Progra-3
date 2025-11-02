package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.RecetaTableModel;

import javax.swing.*;
import java.awt.*;

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

    public HistoricoRecetasView(JFrame parent) {
        this.overlay = new LoadingOverlay(parent);
        recetasTable.setModel(new RecetaTableModel());
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    public void showLoading(boolean visible) { overlay.show(visible); }
    public JComboBox<String> getComboBusqueda() { return comboBusqueda; }
    public JTextField getDatoPacienteField() { return DatoPacienteField; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getDetallesButton() { return DetallesButton; }
    public JTable getRecetasTable() { return recetasTable; }
    public RecetaTableModel getTableModel() { return (RecetaTableModel) recetasTable.getModel(); }
    public JLabel getPacienteInfoLabel() { return pacienteInfoLabel; }
    public JLabel getTelefonoLabel() { return telefonoLabel; }
    public JLabel getFechaNacimientoLabel() { return fechaNacimientoLabel; }

    private void createUIComponents() {
        DetallesButton = new BlueRoundedButton("Detalles");
        buscarButton = new BlueRoundedButton("Buscar");
    }
}


