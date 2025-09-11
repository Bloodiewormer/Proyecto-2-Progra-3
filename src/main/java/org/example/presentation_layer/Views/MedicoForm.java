package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.MedicoController;
import org.example.service_layer.UsuarioService;

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
    private JTextField IDtextFiel;
    private JTextField EspecialidadtextField;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;

    private MedicoController medicoController;

    public MedicoForm(UsuarioService usuarioService) {
        medicoController = new MedicoController(this, usuarioService);

        buscarButton.addActionListener(e -> medicoController.buscarMedico());
        guardarButton.addActionListener(e -> medicoController.guardarMedico());
        limpiarButton.addActionListener(e -> medicoController.limpiarCampos());
        borrarButton.addActionListener(e -> medicoController.borrarMedico());
    }

    public JTable getMedicostable() { return Medicostable; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getIDtextFiel() { return IDtextFiel; }
    public JTextField getEspecialidadtextField() { return EspecialidadtextField; }
    public JTextField getNametextField() { return NametextField; }
    public JPanel getMainPanel() { return MainPanel; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
    }
}
