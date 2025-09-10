package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.PacienteController;
import org.example.service_layer.PacienteService;

import javax.swing.*;

public class PacienteForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel PacientePanel;
    private JPanel TablePacientes;
    private JTable Pacientestable;
    private JPanel BusquedaPaciente;
    private JTextField BuscartextField;
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

    private final PacienteController pacienteController;

    public PacienteForm(PacienteService pacienteService) {
        pacienteController = new PacienteController(this, pacienteService);

        buscarButton.addActionListener(e -> pacienteController.buscarPaciente());
        guardarButton.addActionListener(e -> pacienteController.guardarPaciente());
        limpiarButton.addActionListener(e -> pacienteController.limpiarCampos());
        borrarButton.addActionListener(e -> pacienteController.borrarPaciente());
    }


    public JTable getPacientestable() { return Pacientestable; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getIDtextFiel() { return IDTextField; }
    public JTextField getNametextField() { return NameTextField; }
    public JTextField getTelefonotextField() { return PhoneField; }

    public JButton getBuscarButton() { return buscarButton; }
    public JButton getReporteButton() { return reporteButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JPanel getMainPanel() { return MainPanel; }



    private void createUIComponents() {
        buscarButton = new BlueRoundedButton( "Buscar");
        reporteButton = new BlueRoundedButton( "Reporte");
        guardarButton = new BlueRoundedButton( "Guardar");
        limpiarButton = new BlueRoundedButton( "Limpiar");
        borrarButton = new BlueRoundedButton( "Borrar");

    }



}
