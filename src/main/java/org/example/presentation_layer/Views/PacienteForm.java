package org.example.presentation_layer.Views;

import com.toedter.calendar.JDateChooser;
import org.example.domain_layer.Paciente;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.PacienteController;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.PacienteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.Date;

public class PacienteForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel PacientePanel;
    private JPanel TablePacientes;
    private JTable Pacientestable;
    private JPanel BusquedaPaciente;
    private JTextField BuscarTextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerPaciente;
    private JTextField IDTextField;
    private JTextField NameTextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField PhoneField;
    private JPanel DatePickerPanel;
    private JButton actualizarButton;
    private JDateChooser DatePicker;


    private PacienteTableModel pacienteModel;
    private final PacienteController pacienteController;

    public PacienteForm(PacienteService pacienteService) {
        this.pacienteModel = new PacienteTableModel(pacienteService.leerTodos());
        this.pacienteController = new PacienteController(this, pacienteService, pacienteModel);
        initDatePickers();

        Pacientestable.setModel(pacienteModel);

        buscarButton.addActionListener(e -> pacienteController.buscarPaciente());
        guardarButton.addActionListener(e -> pacienteController.guardarPaciente());
        limpiarButton.addActionListener(e -> pacienteController.limpiarCampos());
        borrarButton.addActionListener(e -> pacienteController.borrarPaciente());
        actualizarButton.addActionListener(e -> pacienteController.actualizarPaciente());
        reporteButton.addActionListener(e -> pacienteController.generarReportePacienteSeleccionado() );
        Pacientestable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }
    //Selectores de fecha
    private void initDatePickers() {
        DatePickerPanel.setLayout(new BorderLayout());

        DatePicker = new JDateChooser();
        DatePicker.setDate(new Date());
        DatePickerPanel.add(DatePicker, BorderLayout.CENTER);

        DatePickerPanel.revalidate();
        DatePickerPanel.repaint();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (pacienteModel == null) return;
        int row = Pacientestable.getSelectedRow();
        if (row < 0) return;
        Paciente p = pacienteModel.getPacienteAt(row);
        if (p == null) return;

        IDTextField.setText(String.valueOf(p.getId()));
        NameTextField.setText(p.getNombre());
        PhoneField.setText(p.getTelefono());
        DatePicker.setDate(p.getFechaNacimiento());
    }





    public JTable getPacientestable() { return Pacientestable; }
    public JTextField getBuscartextField() { return BuscarTextField; }
    public JTextField getIDtextFiel() { return IDTextField; }
    public JTextField getNametextField() { return NameTextField; }
    public JTextField getTelefonotextField() { return PhoneField; }
    public JDateChooser getDatePicker() { return DatePicker; }
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
        actualizarButton = new BlueRoundedButton( "Actualizar");

    }



}
