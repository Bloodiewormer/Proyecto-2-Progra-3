package org.example.Presentation.Views;

import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.MedicamentoTableModel;

import javax.swing.*;

public class MedicamentoForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicamentoPanel;
    private JPanel TableMedicamentos;
    private JTable Medicamentotable;
    private JPanel BusquedaMedicamentos;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerMedicamento;
    private JTextField CodigotextField;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JLabel Codigolable;
    private JLabel NameLable;
    private JTextField PresentaciontextField;
    private JButton actualizarButton;

    private final MedicamentoTableModel tableModel;
    private final LoadingOverlay loadingOverlay;

    public MedicamentoForm(JFrame parentFrame) {
        this.tableModel = new MedicamentoTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);

        Medicamentotable.setModel(tableModel);
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void clearFields() {
        CodigotextField.setText("");
        NametextField.setText("");
        PresentaciontextField.setText("");
        Medicamentotable.clearSelection();
    }

    public void populateFields(MedicamentoResponseDto medicamento) {
        CodigotextField.setText(String.valueOf(medicamento.getId()));
        NametextField.setText(medicamento.getNombre());
        PresentaciontextField.setText(medicamento.getPresentacion());
    }

    // Getters
    public MedicamentoTableModel getTableModel() { return tableModel; }
    public JPanel getMainPanel() { return MainPanel; }
    public JTable getMedicamentotable() { return Medicamentotable; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getCodigotextField() { return CodigotextField; }
    public JTextField getNametextField() { return NametextField; }
    public JTextField getPresentaciontextField() { return PresentaciontextField; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getReporteButton() { return reporteButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getActualizarButton() { return actualizarButton; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
        actualizarButton = new BlueRoundedButton("Actualizar");
    }
}