package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;

import javax.swing.*;

public class PacienteForm {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicoPanel;
    private JPanel TableMedicos;
    private JTable table1;
    private JPanel BusquedaPaciente;
    private JTextField textField1;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerPaciente;
    private JTextField IDTextField;
    private JTextField textField3;
    private JTextField NameTextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField PhoneField;

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton( "Buscar");
        reporteButton = new BlueRoundedButton( "Reporte");
        guardarButton = new BlueRoundedButton( "Guardar");
        limpiarButton = new BlueRoundedButton( "Limpiar");
        borrarButton = new BlueRoundedButton( "Borrar");

    }
}
