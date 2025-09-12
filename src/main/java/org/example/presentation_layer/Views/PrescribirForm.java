package org.example.presentation_layer.Views;

import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.UsuarioService;

import javax.swing.*;

public class PrescribirForm {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton button1;
    private JButton button2;
    private JPanel RectaPanel;
    private JTable MedicamentoRecetatable;
    private JButton guardarButton;
    private JButton detallesButton;
    private JButton limpiarButton;
    private JButton descartarButton;
    private JPanel BuscarPacienteLable;
    private JComboBox filtrarPacientecomboBox;
    private JTextField busquedaPacientetextField;
    private JTable pacientesTable;
    private JPanel AgregarMedicamento;
    private JComboBox FiltraMedicamentoComboBox;
    private JTextField BusquedaMedicamentoTextField;
    private JTable MedicamentosTable;
    private JSpinner catidadSpinner;
    private JSpinner DuracionSpinner;
    private JTextField textField1;
    private JButton guardarButton1;
    private JButton cancerlarButton;
    private JPanel DetallePanel;

    public PrescribirForm(UsuarioService usuarioService, PacienteService pacienteService, MedicamentoService medicamentoService, int idMedico) {

        MedicamentosTable.setModel(new MedicamentoTableModel(medicamentoService.leerTodos()));
        pacientesTable.setModel(new PacienteTableModel(pacienteService.leerTodos()));

    }

    public  JPanel getMainPanel() {
        return mainPanel;
    }
}
