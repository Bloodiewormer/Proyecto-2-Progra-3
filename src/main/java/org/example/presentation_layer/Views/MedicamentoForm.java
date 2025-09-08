package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;

import javax.swing.*;

public class MedicamentoForm {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicamentoPanel;
    private JPanel TableMedicamentos;
    private JTable table1;
    private JPanel BusquedaMedicamentos;
    private JTextField textField1;
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

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        buscarButton = new BlueRoundedButton( "Buscar");
        reporteButton = new BlueRoundedButton( "Reporte");
        guardarButton = new BlueRoundedButton( "Guardar");
        limpiarButton = new BlueRoundedButton( "Limpiar");
        borrarButton = new BlueRoundedButton( "Borrar");

    }
}
