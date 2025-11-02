package org.example.Presentation.Views;

import org.example.Domain.Dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.FarmaceutaTableModel;

import javax.swing.*;
import java.awt.*;

public class FarmaceutaForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel FarmaceutaPanel;
    private JPanel TableFarmaceuta;
    private JTable Farmaceutastable;
    private JPanel BusquedaFarmaceuta;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerFarmaceuta;
    private JTextField IDtextFiel;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private final FarmaceutaTableModel tableModel;
    private final LoadingOverlay loadingOverlay;

    public FarmaceutaForm(JFrame parentFrame) {
        this.tableModel = new FarmaceutaTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);
        Farmaceutastable.setModel(tableModel);

        // Solución: agregar el MainPanel al propio FarmaceutaForm
        setLayout(new BorderLayout());
        add(MainPanel, BorderLayout.CENTER);
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void clearFields() {
        IDtextFiel.setText("");
        NametextField.setText("");
        Farmaceutastable.clearSelection();
    }

    public void populateFields(FarmaceutaResponseDto farmaceuta) {
        IDtextFiel.setText(String.valueOf(farmaceuta.getId()));
        NametextField.setText(farmaceuta.getNombre());
    }

    // Getters
    public FarmaceutaTableModel getTableModel() { return tableModel; }
    public JPanel getMainPanel() { return MainPanel; }
    public JTextField getIDtextFiel() { return IDtextFiel; }
    public JTextField getNametextField() { return NametextField; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTable getFarmaceutatable() { return Farmaceutastable; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getReporteButton() { return reporteButton; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
        actualizarButton = new BlueRoundedButton("Actualizar");
    }
}
