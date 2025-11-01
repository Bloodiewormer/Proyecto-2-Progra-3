package org.example.Presentation.Views;

import org.example.Domain.Dtos.Medico.MedicoResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.MedicoTableModel;

import javax.swing.*;

public class MedicoForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicoPanel;
    private JPanel ManagerMedicos;
    private JPanel BusquedaMedicos;
    private JPanel TableMedicos;
    private JTable Medicostable;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JTextField idTextField;
    private JTextField EspecialidadtextField;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private final MedicoTableModel tableModel;
    private final LoadingOverlay loadingOverlay;

    public MedicoForm(JFrame parentFrame) {
        this.tableModel = new MedicoTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);

        Medicostable.setModel(tableModel);
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void clearFields() {
        idTextField.setText("");
        NametextField.setText("");
        EspecialidadtextField.setText("");
        Medicostable.clearSelection();
    }

    public void populateFields(MedicoResponseDto medico) {
        idTextField.setText(String.valueOf(medico.getId()));
        NametextField.setText(medico.getNombre());
        EspecialidadtextField.setText(medico.getEspecialidad());
    }

    // Getters
    public MedicoTableModel getTableModel() { return tableModel; }
    public JPanel getMainPanel() { return MainPanel; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getIDtextField() { return idTextField; }
    public JTextField getEspecialidadtextField() { return EspecialidadtextField; }
    public JTextField getNametextField() { return NametextField; }
    public JTable getMedicostable() { return Medicostable; }
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